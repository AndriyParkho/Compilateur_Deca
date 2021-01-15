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
    	AbstractExpr nouvelleExpression = new Not(new And(new Not(getLeftOperand()), new Not(getRightOperand())));
    	nouvelleExpression.codeGenSaut(compiler, evaluation, etiquette, op);
    }

}
