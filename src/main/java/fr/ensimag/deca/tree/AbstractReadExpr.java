package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CompilerInstruction;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 * read...() statement.
 *
 * @author gl10
 * @date 01/01/2021
 */
public abstract class AbstractReadExpr extends AbstractExpr {

    public AbstractReadExpr() {
        super();
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
		return false;
	}

	@Override
	protected void codeGenExpr(DecacCompiler compiler, GPRegister op) {
		// FAIT
		compiler.addInstruction(this.getMnemo());
		
		String nomErr = "io_error" + this.getLocation().getLine() + "_" + this.getLocation().getPositionInLine();
		String errorMessage = "Erreur : Input/Output erreur ligne " + this.getLocation().getLine() + " position " + this.getLocation().getPositionInLine();
		CompilerInstruction.createErreurLabel(compiler, nomErr, errorMessage, false);
		
		compiler.addInstruction(new LOAD(Register.R1, op));
	}
	
	protected abstract Instruction getMnemo();

}
