package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.DValGetter;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FloatType;

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
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
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
	protected void codeGenExpr(DecacCompiler compiler, GPRegister op) {
		// A FAIRE : ne fonctionne pas
		if(this.getOperand().getType().isInt()) {
			compiler.addInstruction(new FLOAT(DValGetter.getDVal(this.getOperand()), op));
		} else {
			this.getOperand().codeGenExpr(compiler, op);
		}
	}
}
