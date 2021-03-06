package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CompilerInstruction;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.DIV;
import fr.ensimag.ima.pseudocode.instructions.QUO;

/**
 *
 * @author gl10
 * @date 01/01/2021
 */
public class Divide extends AbstractOpArith {
    public Divide(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "/";
    }


	@Override
	protected Instruction getMnemo(DVal op1, GPRegister op2) {
		// FAIT
		if(super.getType().isInt()) {
			return new QUO(op1, op2);
		} else if(super.getType().isFloat()) {
			return new DIV(op1, op2);
		} else {
			throw new UnsupportedOperationException("Not supposed to be called");
		}
	}


	@Override
	protected void printErrLabel(DecacCompiler compiler) {
		String zero = "0";
		if(this.getType().isFloat()) zero = "0.0";
		String nom = "zero_divide_error_" + this.getLocation().getLine() + "_" + this.getLocation().getPositionInLine();
		String msgError = "Erreur: Division par "+ zero +" non autorisée ligne " + this.getLocation().getLine() + " position " + this.getLocation().getPositionInLine();
		CompilerInstruction.codeGenErreur(compiler, new BOV(CompilerInstruction.createErreurLabel(compiler, nom, msgError)));
	}


	@Override
	protected boolean isDivide() {
		return true;
	}


	@Override
	protected boolean isModulo() {
		return false;
	}
	
	
}
