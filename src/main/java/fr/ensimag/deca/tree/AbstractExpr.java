package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WFLOATX;
import fr.ensimag.ima.pseudocode.instructions.WINT;
import fr.ensimag.ima.pseudocode.instructions.WSTR;

/**
 * Expression, i.e. anything that has a value.
 *
 * @author gl10
 * @date 01/01/2021
 */
public abstract class AbstractExpr extends AbstractInst {
    /**
     * @return true if the expression does not correspond to any concrete token
     * in the source code (and should be decompiled to the empty string).
     */
    boolean isImplicit() {
        return false;
    }

    /**
     * Get the type decoration associated to this expression (i.e. the type computed by contextual verification).
     */
    public Type getType() {
        return type;
    }

    protected void setType(Type type) {
        Validate.notNull(type);
        this.type = type;
    }
    private Type type;

    @Override
    protected void checkDecoration() {
        if (getType() == null) {
            throw new DecacInternalError("Expression " + decompile() + " has no Type decoration");
        }
    }

    /**
     * Verify the expression for contextual error.
     * 
     * implements non-terminals "expr" and "lvalue" 
     *    of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler  (contains the "env_types" the "env_exp" attribute)
     * @param currentClass
     *            Definition of the class containing the expression
     *            (corresponds to the "class" attribute)
     *             is null in the main bloc.
     * @return the Type of the expression
     *            (corresponds to the "type" attribute)
     */
    public abstract Type verifyExpr(DecacCompiler compiler,
            ClassDefinition currentClass)
            throws ContextualError;

    /**
     * Verify the expression in right hand-side of (implicit) assignments 
     * 
     * implements non-terminal "rvalue" of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler  contains the "env_types" and the "env_exp" attribute
     * @param currentClass corresponds to the "class" attribute
     * @param expectedType corresponds to the "type1" attribute            
     * @return this with an additional ConvFloat if needed...
     */
    public AbstractExpr verifyRValue(DecacCompiler compiler,
             ClassDefinition currentClass,
            Type expectedType)
            throws ContextualError {
    	Type typeReel;
    	try {
    		typeReel=this.verifyExpr(compiler, currentClass);
    	} catch(ContextualError ce) { throw ce;}
    	//on vérifie d'abord s'il s'agit d'une classe
		if(typeReel.isClass())
    	{
			if(expectedType.isClass())
        	{
        		ClassType classReelle= (ClassType) typeReel;
        		ClassType classAttendue= (ClassType) expectedType;
        		if(!classReelle.isSubClassOf(classAttendue))
        			throw new ContextualError(String.format("%s ne peut être cast en %s", classReelle, classAttendue),this.getLocation());
        	}
    		else
    		{
    			throw new ContextualError("un type class était attendu!",this.getLocation());
    		}
    	}
    	//sinon, pour deux types différents , deux  possibilité: float=int ou null in object
		else if(!typeReel.sameType(expectedType))
    	{
    		if(typeReel.isInt() && expectedType.isFloat())
    		{
    			ConvFloat entierEnReel= new ConvFloat(this);
				entierEnReel.verifyExpr(compiler, currentClass);
				entierEnReel.setType(expectedType);
				return entierEnReel;
    		}
    		else if (!(typeReel.isNull() && expectedType.isClass()))
    		{
    			throw new ContextualError("type incompatible",this.getLocation());
    		}
    	}
		this.setType(typeReel);
    	return this;
    	
        
    	
    }
    
    
    @Override
    protected void verifyInst(DecacCompiler compiler,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
    	try {
    		this.verifyExpr(compiler, currentClass);
    	} catch (ContextualError ce) {throw ce;}
    }

    /**
     * Verify the expression as a condition, i.e. check that the type is
     * boolean.
     *
     * @param currentClass
     *            Definition of the class containing the expression, or null in
     *            the main program.
     */
    void verifyCondition(DecacCompiler compiler,
            ClassDefinition currentClass) throws ContextualError {
       
    	Type type;
    	try {
    		type=this.verifyExpr(compiler, currentClass);
    	} catch (ContextualError ce) {throw ce;}
    	//il faut juste alors vérifier si type est bien un booléen
    	if(!type.isBoolean())
    	{
    		throw new ContextualError("la condition doit être de type boolean",this.getLocation());
    	}
    }

    /**
     * Generate code to print the expression
     *
     * @param compiler
     */
    protected void codeGenPrint(DecacCompiler compiler, boolean printHex) {
    	//FAIT : traiter codeGenPrint pour les autres types que des chaînes de caractère
    	if(type.isString()) {
    		StringLiteral newThis= (StringLiteral) this;
    	
    		compiler.addInstruction(new WSTR(newThis.getValue()));
    	}
    	else if(type.isFloat()) {
    		GPRegister r = compiler.getRegisterStart();
    		this.codeGenExpr(compiler, r);
    		compiler.addInstruction(new LOAD(r, Register.R1));
    		compiler.freeRegister(r);
    		Instruction printInst = printHex ? new WFLOATX() : new WFLOAT();
    		compiler.addInstruction(printInst);
    	}
    	
    	else if(type.isInt()) {
    		GPRegister r = compiler.getRegisterStart();
    		this.codeGenExpr(compiler, r);
    		compiler.addInstruction(new LOAD(r, Register.R1));
    		compiler.freeRegister(r);
    		compiler.addInstruction(new WINT());
    	}
    	
    	else if(type.isBoolean()) {
    		BooleanLiteral newThis = (BooleanLiteral) this;
    		String valeur = newThis.getValue() ? "true" : "false"; 
    		compiler.addInstruction(new WSTR(valeur));
    	}
    	

    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
    	GPRegister r = compiler.getRegisterStart();
        this.codeGenExpr(compiler, r);
        compiler.freeRegister(r);
    }
    
    /**
     * Fonction ajouté par nous pour pouvoir générer le code des expressions
     * @param compiler
     * @param op
     */
    public abstract void codeGenExpr(DecacCompiler compiler, GPRegister op);
    
    public DVal codeGenAssignDot(DecacCompiler compiler, GPRegister op) {
    	throw new UnsupportedOperationException("L'assign de fonctionne que sur des variables globales ou des champs de classes"); 
    }
    
    @Override
    protected void decompileInst(IndentPrintStream s) {
        decompile(s);
        s.print(";");
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Type t = getType();
        if (t != null) {
            s.print(prefix);
            s.print("type: ");
            s.print(t);
            s.println();
        }
    }
    
    /*
     * Fonction pour savoir si l'expression est un IntLiteral afin d'éviter les instanceOf
     */
    public boolean isIntLiteral(){
    	return false;
    }
    
    /*
     * Fonction pour savoir si l'expression est un FloatLiteral afin d'éviter les instanceOf
     */
    public boolean isFloatLiteral(){
    	return false;
    }
    
    /*
     * Fonction pour savoir si l'expression est un FloatLiteral afin d'éviter les instanceOf
     */
    public boolean isBooleanLiteral(){
    	return false;
    }
    
    /*
     * Fonction pour savoir si l'expression est un Identifier afin d'éviter les instanceOf
     */
    public boolean isIdentifier(){
    	return false;
    }
    
    public boolean isMethod() {
    	return false;
    }
    
    public boolean isDot() {
    	return false;
    }
    
    public boolean isThis() {
    	return false;
    }
    
    public boolean isNull() {
    	return false;
    }
    
    protected void codeGenSaut(DecacCompiler compiler, boolean eval, Label etiquette, GPRegister op) {
    	throw new JumpException("La fonction codeGenSaut n'est pas implémentée pour des expressions de type : " + getType());
    }

}
