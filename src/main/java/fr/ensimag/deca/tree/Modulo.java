package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 *
 * @author gl10
 * @date 01/01/2021
 */
public class Modulo extends AbstractOpArith {

    public Modulo(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        //Fait
    	//on récupère les types des deux opérateurs et on vérifie s'ils sont des int
    	Type typeGauche=this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
    	Type typeDroite=this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
    	if(typeGauche.isInt()&&typeDroite.isInt())
    	{
    		this.setType(typeGauche);
    		return typeGauche;
    	}
    	else
    	{
    		throw new ContextualError("Modulo n'est applicable que sur les entiers",this.getLocation());
    	}
    }


    @Override
    protected String getOperatorName() {
        return "%";
    }

}
