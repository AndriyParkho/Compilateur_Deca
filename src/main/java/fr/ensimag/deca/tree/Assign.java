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
    		this.getRightOperand().setType(typeGauche);
    		this.getRightOperand().verifyRValue(compiler, currentClass, typeGauche);
    		if (compiler.getEnvTypes().get(compiler.getSymbolTable().create(typeGauche.getName().getName())) != null) {
    		    if (compiler.getEnvTypes().get(compiler.getSymbolTable().create(typeGauche.getName().getName())).getType().isClass()) {
                    ClassType left;
                    ClassType right = (ClassType)  this.getRightOperand().getType();
                    if (this.getLeftOperand().isIdentifier()) {
                        left = (ClassType) compiler.getEnvExp().get(((Identifier) this.getLeftOperand()).getName()).getType();
                    }
                    else{
                        left = (ClassType) compiler.getEnvExp().get(compiler.getSymbolTable().create(((Dot) this.getLeftOperand()).getAppel().getName().getName())).getType();
                    }
                    if (left == right || right.isSubClassOf(left) && !left.isSubClassOf(right)) {
                        if (this.getLeftOperand().isIdentifier()) {
                            compiler.getEnvExp().get(((Identifier) this.getLeftOperand()).getName()).setType(this.getRightOperand().getType());
                        }
                        else{
                            compiler.getEnvExp().get(compiler.getSymbolTable().create(((Dot) this.getLeftOperand()).getAppel().getName().getName())).setType(this.getRightOperand().getType());
                        }
                    }else {
                        throw new ContextualError(String.format("%s ne peut être cast en %s", right, left), this.getLocation());
                    }
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
	public void codeGenExpr(DecacCompiler compiler, GPRegister op) {
		getRightOperand().codeGenExpr(compiler, op);
		if(getLeftOperand().isDot()) {
			GPRegister r = compiler.getRegisterStart();
			compiler.addInstruction(new STORE(op, (DAddr) getLeftOperand().codeGenAssignDot(compiler, r)));
			compiler.freeRegister(r);
		} else {
			compiler.addInstruction(new STORE(op, (DAddr) DValGetter.getDVal(getLeftOperand(), compiler)));
		}
	}
}
