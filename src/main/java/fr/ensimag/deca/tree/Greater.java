package fr.ensimag.deca.tree;

import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BGT;
import fr.ensimag.ima.pseudocode.instructions.BLE;
import fr.ensimag.ima.pseudocode.instructions.SGT;

/**
 *
 * @author gl10
 * @date 01/01/2021
 */
public class Greater extends AbstractOpIneq {

    public Greater(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return ">";
    }



	@Override
	protected Instruction getMnemo(GPRegister op) {
		return new SGT(op);
	}


	@Override
	protected Instruction getSaut(Label etiquette) {
		return new BGT(etiquette);
	}


	@Override
	protected Instruction getNotSaut(Label etiquette) {
		return new BLE(etiquette);
	}

}
