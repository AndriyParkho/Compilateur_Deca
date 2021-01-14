package fr.ensimag.deca.tree;

import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.instructions.SGT;

/**
 *
 * @author gl10
 * @date 01/01/2021
 */
public class Lower extends AbstractOpIneq {

    public Lower(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "<";
    }

	@Override
	protected Instruction getMnemo(GPRegister op) {
		return new SGT(op);
	}
}
