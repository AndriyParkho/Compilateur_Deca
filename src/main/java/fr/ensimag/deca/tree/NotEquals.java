package fr.ensimag.deca.tree;

import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.SNE;

/**
 *
 * @author gl10
 * @date 01/01/2021
 */
public class NotEquals extends AbstractOpExactCmp {

    public NotEquals(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "!=";
    }


	@Override
	protected Instruction getMnemo(GPRegister op) {
		return new SNE(op);
	}


	@Override
	protected Instruction getSaut(Label etiquette) {
		return new BNE(etiquette);
	}


	@Override
	protected Instruction getNotSaut(Label etiquette) {
		return new BEQ(etiquette);
	}

}
