package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.DValGetter;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Assignment, i.e. lvalue = expr.
 *
 * @author gl10
 * @date 01/01/2021
 */
public class Assign extends AbstractBinaryExpr {

    @Override
    public AbstractLValue getLeftOperand() {
        // The cast succeeds by construction, as the leftOperand has been set
        // as an AbstractLValue by the constructor.
        return (AbstractLValue)super.getLeftOperand();
    }

    public Assign(AbstractLValue leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
    	//le type de l'opérateur doit etre compatible avec le type de l'opérateur droite
    	//il suffit alors d'appeler la fonction verifyRValue
    	Type typeGauche;
    	try {
    		typeGauche=this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
    		this.getRightOperand().verifyRValue(compiler, localEnv, currentClass, typeGauche);
    	}catch (ContextualError ce) {throw ce;}
    	this.setType(typeGauche);
    	return typeGauche;
    }


    @Override
    protected String getOperatorName() {
        return "=";
    }

	@Override
	protected void codeGenExpr(DecacCompiler compiler, GPRegister op) {
		getRightOperand().codeGenExpr(compiler, op);
		compiler.addInstruction(new STORE(op, (DAddr) DValGetter.getDVal(getLeftOperand())));
	}
}
