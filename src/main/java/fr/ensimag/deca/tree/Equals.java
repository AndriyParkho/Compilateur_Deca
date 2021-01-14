package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
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
	protected Label codeGenSaut(DecacCompiler compiler, String nom) {
		Label labelSaut = new Label(nom);
		compiler.addInstruction(new BEQ(labelSaut));
		return labelSaut;
	}


	
}
