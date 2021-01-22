package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;
import org.apache.log4j.xml.Log4jEntityResolver;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CompilerInstruction;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.TSTO;

/**
 * Declaration of a method
 *
 * @author lagham
 * @date 14/01/2021
 */
public class DeclMethod extends AbstractDeclMethod {
	
	private AbstractIdentifier type;
	private AbstractIdentifier name;
	private ListDeclParam paramList;
	private AbstractMethodBody methodBody;
	
	public AbstractIdentifier getName() {
		return name;
	}
	
	public AbstractIdentifier getType() {
		return type;
	}
	
	public DeclMethod(AbstractIdentifier type , AbstractIdentifier name , ListDeclParam paramList , AbstractMethodBody methodBody)
	{
		this.type=type;
		Validate.notNull(name);
		this.name=name;
		this.paramList=paramList;
		this.methodBody=methodBody;
	}

	
	@Override
	public  void verifyMethodMembers(DecacCompiler compiler  , ClassDefinition currentClass, int index)
            throws ContextualError{
		//FAIT
		EnvironmentExp envGlobClass=currentClass.getMembers();
		ExpDefinition defClassMere=envGlobClass.get(this.name.getName());
		Type typeRetour=this.type.verifyType(compiler);
		ExpDefinition superClassDef=currentClass.getSuperClass().getMembers().get(this.name.getName());
		MethodDefinition methodDef = new MethodDefinition(typeRetour, this.getLocation(), new Signature(), index);
		EnvironmentExp methodEnv=new EnvironmentExp(envGlobClass);
		this.paramList.verifyParamMembers(compiler, methodEnv, currentClass);
		for (AbstractDeclParam param : this.paramList.getList()){
			methodDef.getSignature().add(((DeclParam) param).getType().getType());
		}
		this.name.setDefinition(methodDef);
		this.name.setType(typeRetour);
		//avant de faire la déclaration,il faut vérifier le type de retour et la signature
		//dans le cas d'une redéfinition
		if(superClassDef!=null && superClassDef.isMethod())
		{
			MethodDefinition superMethodDef=(MethodDefinition)superClassDef;
			methodDef.setIndex(superMethodDef.getIndex());
			if(!methodDef.getType().sameType(superMethodDef.getType()))
			{
				throw new ContextualError("type de retour incompatible",this.getLocation());
			}
			else if(methodDef.getSignature().size()!=superMethodDef.getSignature().size())
			{
				throw new ContextualError("Signature incompatible",this.getLocation());
			}
			else
			{
				for(int i=0;i<methodDef.getSignature().size();i++)
				{
					if(!methodDef.getSignature().paramNumber(i).sameType(superMethodDef.getSignature().paramNumber(i)))
					{
						throw new ContextualError("signature incompatible",this.getLocation());
					}
				}
			}
		}

		try {
			currentClass.incNumberOfMethods();
			envGlobClass.declare(this.name.getName(), methodDef);
		}catch(EnvironmentExp.DoubleDefException doubleDef) {
			throw new ContextualError("double définition d'une méthode",this.getLocation());
		}
	}
	
	@Override
    public void verifyMethodBody(DecacCompiler compiler  , ClassDefinition currentClass)
            throws ContextualError{
		//FAIT
		((MethodDefinition)currentClass.getMembers().get(this.name.getName())).setLocalEnv(currentClass.getMembers());
		compiler.setEnvExp(((MethodDefinition)currentClass.getMembers().get(this.name.getName())).getLocalEnv());
		this.paramList.verifyParamBody(compiler, ((MethodDefinition)currentClass.getMembers().get(this.name.getName())).getLocalEnv(), currentClass);
		this.methodBody.verifyMethodBody(compiler, currentClass, this.type.getType());
	}
	
	@Override
    public void decompile(IndentPrintStream s) {
		this.type.decompile(s);
		s.print(" ");
		this.name.decompile(s);
		s.print("(");
		this.paramList.decompile(s);
		s.print(") {");
		this.methodBody.decompile(s);
		s.print(")");
    }
	
	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
			this.type.prettyPrint(s,prefix, false);
			this.name.prettyPrint(s, prefix, false);
			this.paramList.prettyPrint(s, prefix, false);
			this.methodBody.prettyPrint(s, prefix, true);
	    }

	@Override
	protected void iterChildren(TreeFunction f) {
	        throw new UnsupportedOperationException("Not yet supported");
	    }


	@Override
	public void setLabel(String className) {
		name.getMethodDefinition().setLabel(new Label("code." + className + "." + name.getName().getName()));
	}
	
	public void setParamsOperand() {
		for(AbstractDeclParam param : paramList.getList()) {
			param.setParamOperand();
		}
	}
	
	public void codeGenMethod(DecacCompiler compiler, String nomDeLaClasse) {
		compiler.setMaxTempPile(0);
		compiler.setTempPileMethod(0);
		compiler.setCurrentMethod(this);
		CompilerInstruction.decorationLigne(compiler, name.getName().getName());
		compiler.addLabel(compiler.createLabel("code."+nomDeLaClasse+"."+name.getName().getName()));
		int indice = compiler.getLastInstructionIndex();
		name.getMethodDefinition().setDebutBloc(compiler.getLastInstructionIndex());
		compiler.addComment("Sauvegarde des registres");
		saveRegisters(compiler);
		compiler.addComment("Corps de la méthode");
		paramList.codeGenListParam(compiler);
		methodBody.codeGenMethodBody(compiler);
		compiler.addLabel(compiler.createLabel("fin."+ nomDeLaClasse+"."+name.getName().getName()));
		compiler.addComment("Restauration des registres");
		restoreRegisters(compiler);
		compiler.addInstruction(new RTS());
		if(compiler.getCountLB() !=0){
			compiler.addInstructionAfter(new ADDSP(compiler.getCountLB()), indice);
		}
		compiler.addInstructionAfter(new BOV(CompilerInstruction.createErreurLabel(compiler, "stack_overflow_error", "Erreur : pile pleine")), indice);
		compiler.addInstructionAfter(new TSTO(compiler.getCountLB() + compiler.getMaxTempPileMethod()), indice);
	}
	
	private void saveRegisters(DecacCompiler compiler) {
		compiler.incementTempPileMethod();
		compiler.addInstruction(new PUSH(GPRegister.getR(2))); //ici on met les adresses des objets dans R2 (par défaut)
		compiler.setRegisterStart(3); //on met les résultats des expression dans R3
		compiler.incementTempPileMethod();
		compiler.addInstruction(new PUSH(GPRegister.getR(3)));		
	}
	
	private void restoreRegisters(DecacCompiler compiler) {
		compiler.decrementTempPile();
		compiler.addInstruction(new POP(GPRegister.getR(3)));
		compiler.decrementTempPile();
		compiler.addInstruction(new POP(GPRegister.getR(2)));
		compiler.setRegisterStart(2);
			}
	
}
