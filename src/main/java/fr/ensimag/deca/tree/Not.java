package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 *
 * @author gl10
 * @date 01/01/2021
 */
public class Not extends AbstractUnaryExpr {

    public Not(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        //Fait
    	//on vérifie qu'il s'agit bien d'un boolean
    	Type type=this.getOperand().verifyExpr(compiler, localEnv, currentClass);
    	if(type.isBoolean())
    	{
    		this.setType(type);
    		return type;
    	}
    	else
    	{
    		throw new ContextualError("type boolean attendu",this.getLocation());
    	}
    }


    @Override
    protected String getOperatorName() {
        return "!";
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
