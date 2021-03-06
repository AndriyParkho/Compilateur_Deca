package fr.ensimag.deca.tree;

import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.SEQ;

/**
 *
 * @author gl10
 * @date 01/01/2021
 */
public class Equals extends AbstractOpExactCmp {

    public Equals(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "==";
    }


	@Override
	protected Instruction getMnemo(GPRegister op) {
		return new SEQ(op);
	}

	@Override
	protected Instruction getSaut(Label etiquette) {
		return new BEQ(etiquette);
	}


	@Override
	protected Instruction getNotSaut(Label etiquette) {
		return new BNE(etiquette);
	}
	
}
