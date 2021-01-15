package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BGT;
import fr.ensimag.ima.pseudocode.instructions.BLE;
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


	@Override
	protected Instruction getSaut(Label etiquette) {
		return new BLE(etiquette);
	}


	@Override
	protected Instruction getNotSaut(Label etiquette) {
		return new BGT(etiquette);
	}
	
}
