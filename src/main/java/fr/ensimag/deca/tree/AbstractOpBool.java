package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 *
 * @author gl10
 * @date 01/01/2021
 */
public abstract class AbstractOpBool extends AbstractBinaryExpr {

    public AbstractOpBool(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
    	//d'abord on récupère les deux opérateurs droite et gauche
    	Type typeGauche;
    	Type typeDroite;
    	try {
    		typeGauche=this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
    		typeDroite=this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
    	} catch (ContextualError ce) {throw ce;}
    	//il faut ensuite s'assurer que les deux opérateurs sont des booléens
    	if((!typeGauche.isBoolean())||(!typeDroite.isBoolean()))
    	{
    		throw new ContextualError("les deux opérateurs doivent étre de type boolean",this.getLocation());
    	}
    	this.setType(typeDroite);
    	return typeDroite;
    }
    
    protected void codeGenExpr(DecacCompiler compiler, GPRegister R) {
    	//A FAIRE : generation de code pour un opérateur booléen
    	throw new UnsupportedOperationException("not yet implemented");
    }
    
    protected abstract Instruction getMnemo(DVal op1, GPRegister op2);
    

}
