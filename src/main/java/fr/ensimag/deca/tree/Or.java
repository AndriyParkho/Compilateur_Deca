package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;

/**
 *
 * @author gl10
 * @date 01/01/2021
 */
public class Or extends AbstractOpBool {

    public Or(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "||";
    }
    
    @Override
    protected void codeGenSaut(DecacCompiler compiler, boolean evaluation, Label etiquette, GPRegister op) {
    	Not notLeft = new Not(getLeftOperand());
    	notLeft.setLocation(getLeftOperand().getLocation());
    	Not notRight = new Not(getRightOperand());
    	notRight.setLocation(getRightOperand().getLocation());
    	And andNot = new And(notLeft, notRight);
    	andNot.setLocation(this.getLocation());
    	Not notAnd = new Not(andNot);
    	notAnd.setLocation(this.getLocation());
    	notAnd.codeGenSaut(compiler, evaluation, etiquette, op);
    }

}
