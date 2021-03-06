package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CompilerInstruction;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BOV;
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
	public void codeGenExpr(DecacCompiler compiler, GPRegister op) {
		// FAIT
		compiler.addInstruction(this.getMnemo());
		
		String nomErr = "io_error" + this.getLocation().getLine() + "_" + this.getLocation().getPositionInLine();
		String errorMessage = "Erreur : Input/Output erreur ligne " + this.getLocation().getLine() + " position " + this.getLocation().getPositionInLine();
		CompilerInstruction.codeGenErreur(compiler, new BOV(CompilerInstruction.createErreurLabel(compiler, nomErr, errorMessage)));
		
		compiler.addInstruction(new LOAD(Register.R1, op));
	}
	
	protected abstract Instruction getMnemo();	

}
