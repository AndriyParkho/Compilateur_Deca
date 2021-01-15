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
	protected Instruction getMnemo(DVal op1, GPRegister op2) {
		//A FAIRE : générer code pour ADD
		throw new UnsupportedOperationException("not yet implemented");
	}
	
	@Override
	protected void codeGenSaut(DecacCompiler compiler, Boolean evaluation, Label etiquette, GPRegister op) {
		if(evaluation) {
			Label finAnd = new Label("finAnd." + getLocation().getLine() + "."+getLocation().getPositionInLine());
			getLeftOperand().codeGenSaut(compiler, false, finAnd);
			getRightOperand().codeGenSaut(compiler, true, etiquette);
			compiler.addLabel(finAnd);
		}
		else {
			getLeftOperand().codeGenSaut(compiler, false, etiquette);
			getRightOperand().codeGenSaut(compiler, false, etiquette);
		}
	}


}
