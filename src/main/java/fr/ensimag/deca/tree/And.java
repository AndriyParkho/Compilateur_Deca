package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;

/**
 *
 * @author gl10
 * @date 01/01/2021
 */
public class And extends AbstractOpBool {

    public And(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "&&";
    }

	
	@Override
	protected void codeGenSaut(DecacCompiler compiler, boolean evaluation, Label etiquette, GPRegister op) {
		if(evaluation) {
			Label finAnd = new Label("finAnd." + getLocation().getLine() + "."+getLocation().getPositionInLine());
			getLeftOperand().codeGenSaut(compiler, false, finAnd, op);
			getRightOperand().codeGenSaut(compiler, true, etiquette, op);
			compiler.addLabel(finAnd);
		}
		else {
			getLeftOperand().codeGenSaut(compiler, false, etiquette, op);
			getRightOperand().codeGenSaut(compiler, false, etiquette, op);
		}
	}


}
