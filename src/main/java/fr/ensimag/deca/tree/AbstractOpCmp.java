package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;

import java.util.Objects;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.DValGetter;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.BooleanType;


/**
 *
 * @author gl10
 * @date 01/01/2021
 */
public abstract class AbstractOpCmp extends AbstractBinaryExpr {

    public AbstractOpCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
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
    	//il faut ensuite s'assurer que les deux opérateurs sont comparables!!
    	//si les deux deux opérateurs ont deux types différents, la seule comparaison
    	//possible est int/float
    	if(!typeGauche.sameType(typeDroite))
    	{
    		if((typeGauche.isInt()&&typeDroite.isFloat()))
    		{
    			ConvFloat gaucheConv= new ConvFloat(this.getLeftOperand());
    			typeGauche= gaucheConv.verifyExpr(compiler, localEnv, currentClass);
    			gaucheConv.setType(typeGauche);
    			this.setLeftOperand(gaucheConv);
    			
    		}
    		else if((typeGauche.isFloat()&&typeDroite.isInt()))
    		{
    			ConvFloat droiteConv= new ConvFloat(this.getRightOperand());
    			typeDroite= droiteConv.verifyExpr(compiler, localEnv, currentClass);
    			droiteConv.setType(typeDroite);
    			this.setRightOperand(droiteConv);
    			
    		}
    		else
    		{
    			throw new ContextualError("types non compatibles pour faire la comparaison",this.getLocation());
    		}
    	}
    	else   //les deux types sont identiques
    	{
    		//si type=booleen,class ou null , la comparaison n'est possible que pour "="ou"!="
    		if((typeGauche.isBoolean()&&typeDroite.isBoolean())||((typeGauche.isClassOrNull()&&typeDroite.isClassOrNull())))
    		{
    			if(!((Objects.equals(this.getOperatorName(),"==")||(Objects.equals(this.getOperatorName(),"!=")))))
    			{
    				throw new ContextualError("types non compatibles pour faire la comparaison",this.getLocation());
    			}
    		}
    		
    	}
    	//le type de retour est boolean 
    	//la méthode n'est pas encore défini !!! il dans passer par DecacCompiler.java le rep deca!!
    	Type typeRetour = new BooleanType(compiler.getSymbolTable().create("boolean"));
        this.setType(typeRetour);
        return typeRetour;
    	
    }
    
    protected void codeGenExpr(DecacCompiler compiler, GPRegister op) {
    	DVal rightDval = DValGetter.getDVal(getLeftOperand());
    	int numeroRegistre = op.getNumber();
    	if(rightDval != null) {
    		getLeftOperand().codeGenExpr(compiler, op);
    		compiler.addInstruction(new CMP(rightDval, op), "Afin de tester "+getOperatorName()); //il faut ajouter l'outil de comparaison, quoi qu'on fasse de l'opération booléenne
    		compiler.addInstruction(this.getMnemo(op));
    	}else {
    		if(numeroRegistre == compiler.getNombreRegistres()) {
    			getLeftOperand().codeGenExpr(compiler, op);
    			compiler.addInstruction(new PUSH(op));
    			getRightOperand().codeGenExpr(compiler, op);
    			compiler.addInstruction(new LOAD(op, Register.R0));
    			compiler.addInstruction(new POP(op));
    			compiler.addInstruction(new CMP(rightDval, op), "Afin de tester "+getOperatorName());
    			compiler.addInstruction(this.getMnemo(op));
    			
    		} else if(numeroRegistre < compiler.getNombreRegistres()) {
    			GPRegister nextOp = Register.getR(op.getNumber() + 1);
    			getLeftOperand().codeGenExpr(compiler, op);
        		getRightOperand().codeGenExpr(compiler, nextOp);
        		compiler.addInstruction(new CMP(nextOp, op), "Afin de tester "+getOperatorName());
        		compiler.addInstruction(this.getMnemo(op));
    		}
    	}
    }
    
    protected abstract Instruction getMnemo(GPRegister op);
}
