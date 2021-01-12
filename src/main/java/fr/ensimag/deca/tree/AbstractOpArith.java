package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.DValGetter;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Arithmetic binary operations (+, -, /, ...)
 * 
 * @author gl10
 * @date 01/01/2021
 */
public abstract class AbstractOpArith extends AbstractBinaryExpr {

    public AbstractOpArith(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
    	//d'abord on récupère les deux opérateurs droite et gauche
    	Type typeGauche;
    	Type typeDroite;
    	try {
    		typeGauche=this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
    		typeDroite=this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
    	} catch (ContextualError ce) {throw ce;}
    	//il faut ensuite s'assurer que les deux opérateurs sont numériques(int or float)
    	if(((!typeGauche.isInt())&&(!typeGauche.isFloat()))||((!typeDroite.isInt())&&(!typeDroite.isFloat())))
    	{
    		throw new ContextualError("les opérateurs doivent ètre de type numérique (int ou float)",this.getLocation());
    	}
    	//reste à vérifier s'il y a des conversions int/float à faire
    	else if(!typeGauche.sameType(typeDroite))
    	{
    		if(typeGauche.isInt()) //implique que typeDroite est un float
    		{
    			ConvFloat gaucheConv= new ConvFloat(this.getLeftOperand());
    			typeGauche= gaucheConv.verifyExpr(compiler, localEnv, currentClass);
    			gaucheConv.setType(typeGauche);
    			this.setLeftOperand(gaucheConv);
    			return typeGauche;
    		}
    		else
    		{
    			ConvFloat droiteConv= new ConvFloat(this.getRightOperand());
    			typeDroite= droiteConv.verifyExpr(compiler, localEnv, currentClass);
    			droiteConv.setType(typeDroite);
    			this.setRightOperand(droiteConv);
    			return typeDroite;
    		}
    	}
    	else //c à d on a le mème type pour les deux opérateurs.
    		return typeDroite;
    }
    
    @Override
    protected void codeGenExpr(DecacCompiler compiler, GPRegister op) {
    	DVal rightDVal = DValGetter.getDVal(getRightOperand());
    	int n = op.getNumber();
    	if(rightDVal != null) {
    		getLeftOperand().codeGenExpr(compiler, op);
    		compiler.addInstruction(this.getMnemo(rightDVal, op));
    	} else {
    		if(n == 15) {
    			getLeftOperand().codeGenExpr(compiler, op);
    			compiler.addInstruction(new PUSH(op));
    			getRightOperand().codeGenExpr(compiler, op);
    			compiler.addInstruction(new LOAD(op, Register.R0));
    			compiler.addInstruction(new POP(op));
    			compiler.addInstruction(this.getMnemo(Register.R0, op));
    		} else if(n < 15) {
    			GPRegister nextOp = Register.getR(op.getNumber() + 1);
    			getLeftOperand().codeGenExpr(compiler, op);
        		getRightOperand().codeGenExpr(compiler, nextOp);
        		compiler.addInstruction(this.getMnemo(nextOp, op));
    		}
    	}
    	
    }
    
    protected abstract Instruction getMnemo(DVal op1, GPRegister op2);
}
