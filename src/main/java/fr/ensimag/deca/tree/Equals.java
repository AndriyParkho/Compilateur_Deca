package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;

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
	protected Instruction getMnemo(DVal op1, GPRegister op2) {
	//A FAIRE
	throw new UnsupportedOperationException("not yet implemented");
	}



	
}
