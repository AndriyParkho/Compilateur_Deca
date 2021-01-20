package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CompilerInstruction;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.SUBSP;
import fr.ensimag.ima.pseudocode.instructions.TSTO;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author gl10
 * @date 01/01/2021
 */
public class DeclClass extends AbstractDeclClass {
	
	private AbstractIdentifier name;
	private AbstractIdentifier superClass;
	private ListDeclMethod methodList;
	private ListDeclField fieldList;
	
	
	public DeclClass(AbstractIdentifier name , AbstractIdentifier superClass , ListDeclMethod methodList , ListDeclField fieldList)
	{
		Validate.notNull(name);
		this.name=name;
		this.superClass=superClass;
		this.methodList=methodList;
		this.fieldList=fieldList;
		
	}
	

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("class ");
        this.name.decompile(s);
        s.print("extends ");
        this.superClass.decompile(s);
        s.println(" {");
        this.fieldList.decompile(s);
        this.methodList.decompile(s);
        s.println(" }");
    }
    /**
     * vérification de la super classe et le nom
     * ensuite , déclaration
     * @param compiler
     * @throws ContextualError
     */
    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
        //FAIT
		if(!this.superClass.verifyType(compiler).isClass())
		{
			throw new ContextualError("le champs superClass doit etre une classe",this.getLocation());
		}
    	ClassDefinition superClassDef=(ClassDefinition)compiler.getEnvTypes().get(compiler.getSymbolTable().create(superClass.getName().getName()));
		if(superClassDef==null)
    	{
    		throw new ContextualError("super classe introuvable",this.getLocation());
    	}
    	else if(compiler.getEnvTypes().get(compiler.getSymbolTable().create(this.name.getName().getName()))!=null)
    	{
    		throw new ContextualError("une telle classe est déja définie",this.getLocation());
    	}
    	//on peut alors déclarer la classe et faire les set
    	ClassType typeClass=new ClassType(this.name.getName(),this.getLocation(),superClassDef);
    	ClassDefinition defClass= typeClass.getDefinition();
    	Symbol symClass=compiler.getSymbolTable().create(this.name.getName().getName());
    	compiler.getEnvTypes().declare(symClass, defClass);
    	this.name.setDefinition(defClass);
    	this.name.setType(typeClass);
    }
    /**
     * on initialise le nombre de champs et de méthode
     * on vérifie la déclaration des champs et des méthodes
     * @param compiler
     */
    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
        //FAIT
    	int numFields=0;
    	int numMethods=0;
    	if(this.superClass.getClassDefinition()!=null)
    	{
    		numFields=this.superClass.getClassDefinition().getNumberOfFields();
    		numMethods=this.superClass.getClassDefinition().getNumberOfMethods();
    	}
    	this.name.getClassDefinition().setNumberOfFields(numFields);
    	this.name.getClassDefinition().setNumberOfMethods(numMethods);
    	ClassDefinition classDef=(ClassDefinition)compiler.getEnvTypes().get(compiler.getSymbolTable().create(this.name.getName().getName()));
    	this.fieldList.verifyFieldMembers(compiler, classDef);
    	this.methodList.verifyMethodMembers(compiler, classDef);
    }
    /**
     * vérification des initialisation des champs et des corps des méthodes 
     * @param compiler
     */
    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        //FAIT
    	ClassDefinition classDef=(ClassDefinition)compiler.getEnvTypes().get(compiler.getSymbolTable().create(this.name.getName().getName()));
    	this.fieldList.verifyFieldBody(compiler, classDef);
    	this.methodList.verifyMethodBody(compiler, classDef);
    }


    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        name.prettyPrint(s,prefix,false);
        superClass.prettyPrint(s,prefix,false);
        fieldList.prettyPrint(s,prefix,false);
        methodList.prettyPrint(s,prefix,true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
    	// A FAIRE
//        throw new UnsupportedOperationException("Not yet supported");
    }


	@Override
	protected void codeGenClassMethodTable(DecacCompiler compiler) {
		// A FAIRE
		ClassDefinition classDef = name.getClassDefinition();
		// Définir le label de chaque méthode et créer le tableau des étiquettes
		for(AbstractDeclMethod method : methodList.getList()) {
			method.setLabel(name.getName().getName());
		}
		classDef.setMethodsTable();
		compiler.addComment("Construction de la table des méthodes de " + this.name.getName().getName());
		// Empiler l'@superclass
		compiler.incrCountGB();
		compiler.addInstruction(new LEA(superClass.getClassDefinition().getOperand(), Register.R0));
		// setOperand de cette class
		classDef.setOperand(new RegisterOffset(compiler.getCountGB(), Register.GB));
		compiler.addInstruction(new STORE(Register.R0, classDef.getOperand()));
		// Générer la table des méthodes
		classDef.codeGenMethodTable(compiler);
	}
	
	@Override
	protected void codeGenClassBody(DecacCompiler compiler) {
		CompilerInstruction.decorationAssembleur(compiler, "Classe "+name.getName().getName());
		codeGenInitClass(compiler);
		compiler.setIsInMethod(true); //on indique au compilateur que l'on se trouve désormais dans une méthode
		//de cette manière, les variables stockées le seront dans le LB.
		methodList.codeGenListMethod(compiler, name.getName().getName());
		compiler.setIsInMethod(false);
	}
	
	protected void codeGenInitClass(DecacCompiler compiler) {
		CompilerInstruction.decorationLigne(compiler, "Initialisation des champs de " + name.getName().getName());
		compiler.addLabel(name.getClassDefinition().getInitLabel());
		boolean haveSuperclass = !"Object".equals(superClass.getName().getName()); 
		
		if(haveSuperclass) {
			compiler.addInstruction(new TSTO(new ImmediateInteger(3)));
			compiler.addInstruction(new BOV(compiler.createLabel("stack_overflow_error")));
			compiler.addInstruction(new LOAD(new RegisterOffset(-2, GPRegister.LB), GPRegister.R1));
		}
		for(AbstractDeclField field : fieldList.getList()) {
			if(haveSuperclass) {
				field.codeGenInitExtendsField(compiler, true);				
			}
			else field.codeGenInitField(compiler);
		}
		
		if(haveSuperclass) {
			compiler.addComment("Appel de l'initialisation des champs hérités de " + superClass.getName().getName());
			compiler.addInstruction(new PUSH(GPRegister.R1));
			compiler.addInstruction(new BSR(superClass.getClassDefinition().getInitLabel()));
			compiler.addInstruction(new SUBSP(new ImmediateInteger(1)));
			
			for(AbstractDeclField field : fieldList.getList()) {
				field.codeGenInitExtendsField(compiler, false);
			}
		}
		compiler.addComment("Retour au code principal");
		compiler.addInstruction(new RTS());
	}
    

}
