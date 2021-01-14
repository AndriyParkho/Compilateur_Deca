package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.SNE;

/**
 *
 * @author gl10
 * @date 01/01/2021
 */
public class NotEquals extends AbstractOpExactCmp {

    public NotEquals(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "!=";
    }


	@Override
	protected Instruction getMnemo(GPRegister op) {
		return new SNE(op);
	}

	@Override
	protected Label codeGenSaut(DecacCompiler compiler, String nom) {
		Label labelSaut = new Label(nom);
		compiler.addInstruction(new BNE(labelSaut));
		return labelSaut;
	}
	
	


}
