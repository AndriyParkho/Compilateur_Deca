package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;

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
	protected void codeGenExpr(DecacCompiler compiler, GPRegister op) {
		// A FAIRE
		throw new UnsupportedOperationException("not yet implemented");
		
	}
}
