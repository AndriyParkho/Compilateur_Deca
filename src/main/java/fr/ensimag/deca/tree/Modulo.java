package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CompilerInstruction;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.REM;

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
    public Type verifyExpr(DecacCompiler compiler,
            ClassDefinition currentClass) throws ContextualError {
        //Fait
    	//on récupère les types des deux opérateurs et on vérifie s'ils sont des int
    	Type typeGauche=this.getLeftOperand().verifyExpr(compiler, currentClass);
    	Type typeDroite=this.getRightOperand().verifyExpr(compiler, currentClass);
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

	@Override
	protected Instruction getMnemo(DVal op1, GPRegister op2) {
		// A FAIRE : vérifier que c'est des entiers
		return new REM(op1, op2);
	}

	@Override
	protected void printErrLabel(DecacCompiler compiler) {
		String zero = "0";
		if(this.getType().isFloat()) zero = "0.0";
		String nom = "zero_modulo_error_" + this.getLocation().getLine() + "_" + this.getLocation().getPositionInLine();
		String msgError = "Erreur: Modulo par " + zero + " non autorisé ligne " + this.getLocation().getLine() + " position " + this.getLocation().getPositionInLine();
		CompilerInstruction.codeGenErreur(compiler, new BOV(CompilerInstruction.createErreurLabel(compiler, nom, msgError)));
	}

	@Override
	protected boolean isDivide() {
		return false;
	}

	@Override
	protected boolean isModulo() {
		return true;
	}
	
	
}
