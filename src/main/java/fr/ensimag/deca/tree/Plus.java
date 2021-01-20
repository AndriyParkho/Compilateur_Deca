package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CompilerInstruction;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.BOV;

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
	protected void printErrLabel(DecacCompiler compiler) {
		String nom = "add_overflow_error_" + this.getLocation().getLine() + "_" + this.getLocation().getPositionInLine();
		String msgError = "Erreur: Overflow pendant l'addition ligne " + this.getLocation().getLine() + " position " + this.getLocation().getPositionInLine();
		compiler.addInstruction(new BOV(CompilerInstruction.createErreurLabel(compiler, nom, msgError)));
	}


	@Override
	protected boolean isDivide() {
		return false;
	}


	@Override
	protected boolean isModulo() {
		return false;
	}

	
}
