package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.BooleanType;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 *
 * @author gl10
 * @date 01/01/2021
 */
public class BooleanLiteral extends AbstractExpr {

    private boolean value;

    public BooleanLiteral(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler,
            ClassDefinition currentClass) throws ContextualError {
        //Fait
    	Type type= new BooleanType(compiler.getSymbolTable().create("boolean"));
    	this.setType(type);
    	return type;
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print(Boolean.toString(value));
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    String prettyPrintNode() {
        return "BooleanLiteral (" + value + ")";
    }

	@Override
	protected void codeGenExpr(DecacCompiler compiler, GPRegister op) {
		int bool = value ? 1 : 0;
		compiler.addInstruction(new LOAD(bool, op));
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
		return true;
	}

	@Override
	public boolean isIdentifier() {
		return false;
	}
	
	@Override
	protected void codeGenSaut(DecacCompiler compiler, boolean eval, Label etiquette, GPRegister op) {
    	if(!value) {
    		(new BooleanLiteral(true)).codeGenSaut(compiler, !eval, etiquette, op);
    	} else {
    		if(eval) {
    			compiler.addInstruction(new BRA(etiquette));
    		}
    	}
    }

}
