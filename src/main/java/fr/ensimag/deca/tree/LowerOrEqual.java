package fr.ensimag.deca.tree;

import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.instructions.SLE;

/**
 *
 * @author gl10
 * @date 01/01/2021
 */
public class LowerOrEqual extends AbstractOpIneq {
    public LowerOrEqual(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "<=";
    }

	@Override
	protected Instruction getMnemo(GPRegister op) {
		return new SLE(op);
	}
}
