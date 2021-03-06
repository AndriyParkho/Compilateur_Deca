package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FloatType;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;

/**
 * Conversion of an int into a float. Used for implicit conversions.
 * 
 * @author gl10
 * @date 01/01/2021
 */
public class ConvFloat extends AbstractUnaryExpr {
    public ConvFloat(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler,
            ClassDefinition currentClass) {
        //Fait
    	Type type= new FloatType(compiler.getSymbolTable().create("float"));
    	this.setType(type);
    	return type;
    }


    @Override
    protected String getOperatorName() {
        return "/* conv float */";
    }

	@Override
	public void codeGenExpr(DecacCompiler compiler, GPRegister op) {
		// FAIT
		this.getOperand().codeGenExpr(compiler, op);
		if(this.getOperand().getType().isInt()) {	
			compiler.addInstruction(new FLOAT(op, op));
		}
	}
}
