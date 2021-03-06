package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CompilerInstruction;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.MUL;

/**
 * @author gl10
 * @date 01/01/2021
 */
public class Multiply extends AbstractOpArith {
    public Multiply(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "*";
    }


	@Override
	protected Instruction getMnemo(DVal op1, GPRegister op2) {
		return new MUL(op1, op2);
	}


	@Override
	protected void printErrLabel(DecacCompiler compiler) {
		String nom = "mult_overflow_error_" + this.getLocation().getLine() + "_" + this.getLocation().getPositionInLine();
		String msgError = "Erreur: Overflow pendant la multiplication ligne " + this.getLocation().getLine() + " position " + this.getLocation().getPositionInLine();
		CompilerInstruction.codeGenErreur(compiler, new BOV(CompilerInstruction.createErreurLabel(compiler, nom, msgError)));
	}


	@Override
	protected boolean isDivide() {
		return false;
	}


	@Override
	protected boolean isModulo() {
		return false;
	}

	
}
