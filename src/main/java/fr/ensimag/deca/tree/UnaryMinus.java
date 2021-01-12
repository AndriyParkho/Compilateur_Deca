package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * @author gl10
 * @date 01/01/2021
 */
public class UnaryMinus extends AbstractUnaryExpr {

    public UnaryMinus(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        //Fait
    	//il faut que le type soit int ou float
    	Type type=this.getOperand().verifyExpr(compiler, localEnv, currentClass);
    	if(type.isInt()||type.isFloat())
    	{
    		this.setType(type);
    		return type;
    	}
    	else
    	{
    		throw new ContextualError("unaryMinus est applicable sur les types numériques",this.getLocation());
    	}
    }

	@Override
    protected String getOperatorName() {
        return "-";
    }

	@Override
	protected void codeGenExpr(DecacCompiler compiler, GPRegister op) {
		// A FAIRE
		throw new UnsupportedOperationException("not yet implemented");
	}

    @Override
	public boolean isIntLiteral() {
		return false;
	}

	@Override
	public boolean isIdentifier() {
		return false;
	}
}
