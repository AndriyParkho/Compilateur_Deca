package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.context.*;
import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.DValGetter;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 * Deca Identifier
 *
 * @author gl10
 * @date 01/01/2021
 */
public class Identifier extends AbstractIdentifier {
    
    @Override
    protected void checkDecoration() {
        if (getDefinition() == null) {
            throw new DecacInternalError("Identifier " + this.getName() + " has no attached Definition");
        }
    }

    @Override
    public Definition getDefinition() {
        return definition;
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * ClassDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a class definition.
     */
    @Override
    public ClassDefinition getClassDefinition() {
        try {
            return (ClassDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a class identifier, you can't call getClassDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * MethodDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a method definition.
     */
    @Override
    public MethodDefinition getMethodDefinition() {
        try {
            return (MethodDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a method identifier, you can't call getMethodDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * FieldDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public FieldDefinition getFieldDefinition() {
        try {
            return (FieldDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a field identifier, you can't call getFieldDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * VariableDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public VariableDefinition getVariableDefinition() {
        try {
            return (VariableDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a variable identifier, you can't call getVariableDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a ExpDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public ExpDefinition getExpDefinition() {
        try {
            return (ExpDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a Exp identifier, you can't call getExpDefinition on it");
        }
    }

    @Override
    public void setDefinition(Definition definition) {
        this.definition = definition;
    }

    @Override
    public Symbol getName() {
        return name;
    }

    private Symbol name;

    public Identifier(Symbol name) {
        Validate.notNull(name);
        this.name = name;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler,
            ClassDefinition currentClass) throws ContextualError {
        //Fait 
    	//si l'identificateur existe dans l'environnement local, on fait l'enrichissement
    	//sinon on lève une erreur contextuelle
        System.out.println(compiler.getEnvExp().get(compiler.getSymbolTable().create(this.name.getName())));
        System.out.println("r");
        if (compiler.getEnvExp().get(compiler.getSymbolTable().create(this.name.getName())) != null) {
            this.setDefinition(compiler.getEnvExp().get(compiler.getSymbolTable().create(this.name.getName())));
            this.setType(compiler.getEnvExp().get(compiler.getSymbolTable().create(this.name.getName())).getType());
            return compiler.getEnvExp().get(compiler.getSymbolTable().create(this.name.getName())).getType();
        }
        else if (compiler.getEnvExp().get(this.name) != null ){
            this.setDefinition(compiler.getEnvExp().get(this.name));
            this.setType(compiler.getEnvExp().get(this.name).getType());
            return compiler.getEnvExp().get(this.name).getType();
        }
        else {
            System.out.println(this.name.getName());
            throw new ContextualError("identificateur non défini", this.getLocation());
        }
    }

    /**
     * Implements non-terminal "type" of [SyntaxeContextuelle] in the 3 passes
     * @param compiler contains "env_types" attribute
     */
    @Override
    public Type verifyType(DecacCompiler compiler) throws ContextualError {
        //Fait
    	//si le type existe dans l'enveloppe des types, on met à jour l'enrichissement 
    	//sinon, on lève une erreur contextuelle
        this.name = compiler.getSymbolTable().create(this.name.getName());
    	if(compiler.getEnvTypes().get(this.name)!=null)
    	{
    		//enrichissement def & type
    		this.setDefinition(compiler.getEnvTypes().get(this.name));
    		Type type= compiler.getEnvTypes().get(this.name).getType();
    		this.setType(type);
    		return type;
    	}
    	else
    	{
    		throw new ContextualError("type non défini",this.getLocation());
    	}
    }
    
    
    private Definition definition;


    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(name.toString());
    }

    @Override
    String prettyPrintNode() {
        return "Identifier (" + getName() + ")";
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Definition d = getDefinition();
        if (d != null) {
            s.print(prefix);
            s.print("definition: ");
            s.print(d);
            s.println();
        }
    }

	@Override
	protected void codeGenExpr(DecacCompiler compiler, GPRegister op) {
		compiler.addInstruction(new LOAD(DValGetter.getDVal(this), op));
	}
	
	@Override
	protected void codeGenSaut(DecacCompiler compiler, boolean eval, Label etiquette, GPRegister op) {
		compiler.addInstruction(new LOAD(DValGetter.getDVal(this), op));
		compiler.addInstruction(new CMP(0, op));
		if(eval) compiler.addInstruction(new BNE(etiquette));
		else compiler.addInstruction(new BEQ(etiquette));
    }

	@Override
	public boolean isIntLiteral() {
		return false;
	}

	@Override
	public boolean isFloatLiteral() {
		return false;
	}

	@Override
	public boolean isBooleanLiteral() {
		return false;
	}

	@Override
	public boolean isIdentifier() {
		return true;
	}

	
}
