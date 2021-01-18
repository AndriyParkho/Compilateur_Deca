package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.STORE;

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
    	ClassDefinition superClassDef=(ClassDefinition)compiler.getEnvTypes().get(this.name.getName());
    	if(superClassDef==null)
    	{
    		throw new ContextualError("super classe introuvable",this.getLocation());
    	}
    	else if(!this.superClass.verifyType(compiler).isClass())
    	{
    		throw new ContextualError("le champs superClass doit etre une classe",this.getLocation());
    	}
    	else if(compiler.getEnvTypes().get(this.name.getName())!=null)
    	{
    		throw new ContextualError("une telle classe est déja définie",this.getLocation());
    	}
    	//on peut alors déclarer la classe et faire les set
    	ClassType typeClass=new ClassType(this.name.getName(),this.getLocation(),superClassDef);
    	ClassDefinition defClass= typeClass.getDefinition();
    	Symbol symClass=compiler.getSymbolTable().create(this.name.getName().toString());
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
    	ClassDefinition classDef=(ClassDefinition)compiler.getEnvTypes().get(this.name.getName());
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
    	ClassDefinition classDef=(ClassDefinition)compiler.getEnvTypes().get(this.name.getName());
    	this.fieldList.verifyFieldBody(compiler, classDef);
    	this.methodList.verifyMethodBody(compiler, classDef);
    }


    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        throw new UnsupportedOperationException("Not yet supported");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        throw new UnsupportedOperationException("Not yet supported");
    }


	@Override
	protected void codeGenClassMethodTable(DecacCompiler compiler) {
		// A FAIRE
		ClassDefinition classDef = name.getClassDefinition();
		// Définir le label de chaque méthode et créer le tableau des étiquettes
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
    

}
