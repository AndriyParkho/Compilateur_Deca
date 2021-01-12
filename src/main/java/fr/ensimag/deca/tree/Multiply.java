package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
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
	protected void codeGenExpr(DecacCompiler compiler, GPRegister op) {
		// A FAIRE
		throw new UnsupportedOperationException("not yet implemented");
	}

	
}
