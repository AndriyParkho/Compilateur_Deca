package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CompilerInstruction;
import fr.ensimag.deca.codegen.DValGetter;
import fr.ensimag.deca.context.*;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.STORE;

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
    public Type verifyExpr(DecacCompiler compiler,
            ClassDefinition currentClass) throws ContextualError {
    	//le type de l'opérateur doit etre compatible avec le type de l'opérateur droite
    	//il suffit alors d'appeler la fonction verifyRValue
    	Type typeGauche;
    	try {
    		typeGauche=this.getLeftOperand().verifyExpr(compiler, currentClass);
    		this.getRightOperand().verifyRValue(compiler, currentClass, typeGauche);
    		if (compiler.getEnvExp().get(((Identifier) this.getLeftOperand()).getName()).getType().isClass()) {
                ClassType left = (ClassType) compiler.getEnvExp().get(((Identifier) this.getLeftOperand()).getName()).getType();
                ClassType right = (ClassType)((New)this.getRightOperand()).getType();
                if (left == right) {
                    compiler.getEnvExp().get(((Identifier) this.getLeftOperand()).getName()).setType(this.getRightOperand().getType());
                } else if (right.isSubClassOf(left) && !left.isSubClassOf(right)) {
                    compiler.getEnvExp().get(((Identifier) this.getLeftOperand()).getName()).setType(this.getRightOperand().getType());
                } else {
                    throw new ContextualError(String.format("%s ne peut etre cast en %s", right, left), this.getLocation());
                }
            }
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
