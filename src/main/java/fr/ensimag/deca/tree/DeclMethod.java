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
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.RTS;

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
		int vraiIndex=index;
		if(defClassMere!=null && defClassMere.isMethod())
		{
			FieldDefinition fieldClassDef= (FieldDefinition)defClassMere;
			vraiIndex=fieldClassDef.getIndex();
		}
		Type typeRetour=this.type.verifyType(compiler);
		MethodDefinition methodDef= new MethodDefinition(typeRetour, this.getLocation(), new Signature(),vraiIndex);
		EnvironmentExp methodEnv=new EnvironmentExp(envGlobClass);
		this.paramList.verifyParamMembers(compiler, methodEnv, currentClass);
		this.name.setDefinition(methodDef);
		this.name.setType(typeRetour);
		//avant de faire la déclaration,il faut vérifier le type de retour et la signature
		//dans le cas d'une redéfinition
		ExpDefinition superClassDef=currentClass.getSuperClass().getMembers().get(this.name.getName());
		if(superClassDef!=null && superClassDef.isMethod())
		{
			MethodDefinition superMethodDef=(MethodDefinition)superClassDef;
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
				EnvironmentExp envMethod=new EnvironmentExp(currentClass.getMembers());
				this.paramList.verifyParamBody(compiler, envMethod, currentClass);
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
	
	
	public void codeGenMethod(DecacCompiler compiler) {
		CompilerInstruction.decorationLigne(compiler, "Sauvegarde des registres");
		saveRegisters(compiler);
		methodBody.codeGenMethodBody(compiler);
		//compiler.addLabel(compiler.createLabel("fin."+));
		CompilerInstruction.decorationLigne(compiler, "Restauration des registres");
		restoreRegisters(compiler);
		compiler.addInstruction(new RTS());
	}
	
	public void saveRegisters(DecacCompiler compiler) {
		compiler.addInstruction(new PUSH(GPRegister.getR(2))); //ici on met les adresses des objets dans R2 (par défaut)
		compiler.setRegisterStart(3); //on met les résultats des epxression dans R3
		compiler.addInstruction(new PUSH(GPRegister.getR(3)));
		//ajouter +2 au variable pour le TSTO		
	}
	
	public void restoreRegisters(DecacCompiler compiler) {
		compiler.addInstruction(new POP(GPRegister.getR(3)));
		compiler.addInstruction(new POP(GPRegister.getR(2)));
		compiler.setRegisterStart(2);
		//enlever 2
	}
	
}
