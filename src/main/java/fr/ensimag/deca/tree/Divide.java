package fr.ensimag.deca.tree;

import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
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

}
