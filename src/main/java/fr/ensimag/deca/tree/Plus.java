package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.instructions.ADD;

/**
 * @author gl10
 * @date 01/01/2021
 */
public class Plus extends AbstractOpArith {
    public Plus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }
 

    @Override
    protected String getOperatorName() {
        return "+";
    }


	@Override
	protected Instruction getMnemo(DVal op1, GPRegister op2) {
		return new ADD(op1, op2); 
	}


	@Override
	protected void codeGenExpr(DecacCompiler compiler, GPRegister op) {
		// A FAIRE
		throw new UnsupportedOperationException("not yet implemented");
		
	}


	@Override
	public boolean isIntLiteral() {
		return false;
	}


	@Override
	public boolean isIdentifier() {
		return false;
	}
	
	
}
