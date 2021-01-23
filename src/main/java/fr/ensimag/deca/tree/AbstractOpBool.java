package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
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
    public Type verifyExpr(DecacCompiler compiler,
            ClassDefinition currentClass) throws ContextualError {
    	//d'abord on récupère les deux opérateurs droite et gauche
    	Type typeGauche;
    	Type typeDroite;
    	try {
    		typeGauche=this.getLeftOperand().verifyExpr(compiler, currentClass);
    		typeDroite=this.getRightOperand().verifyExpr(compiler, currentClass);
    	} catch (ContextualError ce) {throw ce;}
    	//il faut ensuite s'assurer que les deux opérateurs sont des booléens
    	if((!typeGauche.isBoolean())||(!typeDroite.isBoolean()))
    	{
    		throw new ContextualError("les deux opérateurs doivent être de type boolean",this.getLocation());
    	}
    	this.setType(typeDroite);
    	return typeDroite;
    }
    
    public void codeGenExpr(DecacCompiler compiler, GPRegister op) {
    	Label fauxLbl = compiler.createLabel("OpBoolFaux_" + this.getLocation().getLine() + "_" + this.getLocation().getPositionInLine());
		this.codeGenSaut(compiler, false, fauxLbl, op);
		compiler.addInstruction(new LOAD(1, op));
		Label finOpBool = compiler.createLabel("FinOpBool" + this.getLocation().getLine() + "_" + this.getLocation().getPositionInLine());
    	compiler.addInstruction(new BRA(finOpBool));
    	
    	compiler.addLabel(fauxLbl);
    	compiler.addInstruction(new LOAD(0, op));
    	
    	compiler.addLabel(finOpBool);
    }
    

}
