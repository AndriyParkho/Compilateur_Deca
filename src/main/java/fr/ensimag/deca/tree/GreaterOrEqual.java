package fr.ensimag.deca.tree;


import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BGE;
import fr.ensimag.ima.pseudocode.instructions.BLT;
import fr.ensimag.ima.pseudocode.instructions.SGE;

/**
 * Operator "x >= y"
 * 
 * @author gl10
 * @date 01/01/2021
 */
public class GreaterOrEqual extends AbstractOpIneq {

    public GreaterOrEqual(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return ">=";
    }


	@Override
	protected Instruction getMnemo(GPRegister op) {
		return new SGE(op);
	}


	@Override
	protected Instruction getSaut(Label etiquette) {
		return new BGE(etiquette);
	}


	@Override
	protected Instruction getNotSaut(Label etiquette) {
		return new BLT(etiquette);
	}
	
}
