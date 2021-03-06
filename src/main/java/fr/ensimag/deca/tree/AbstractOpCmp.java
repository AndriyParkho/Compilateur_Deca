package fr.ensimag.deca.tree;

import java.util.Objects;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.DValGetter;
import fr.ensimag.deca.context.BooleanType;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;


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
    public Type verifyExpr(DecacCompiler compiler,
            ClassDefinition currentClass) throws ContextualError {
    	//d'abord on récupère les deux opérateurs droite et gauche
    	Type typeGauche;
    	Type typeDroite;
    	try {
    		typeGauche=this.getLeftOperand().verifyExpr(compiler, currentClass);
    		typeDroite=this.getRightOperand().verifyExpr(compiler, currentClass);
    	} catch (ContextualError ce) {throw ce;}
    	//il faut ensuite s'assurer que les deux opérateurs sont comparables!!
    	//si les deux deux opérateurs ont deux types différents, les seules comparaisons
    	//possible sont int/float et class/null pour l'égalité (cf Equals.java)
    	if(!typeGauche.sameType(typeDroite))
    	{
    		if((typeGauche.isInt()&&typeDroite.isFloat()))
    		{
    			ConvFloat gaucheConv= new ConvFloat(this.getLeftOperand());
    			typeGauche= gaucheConv.verifyExpr(compiler, currentClass);
    			gaucheConv.setType(typeGauche);
    			this.setLeftOperand(gaucheConv);
    			
    		}
    		else if((typeGauche.isFloat()&&typeDroite.isInt()))
    		{
    			ConvFloat droiteConv= new ConvFloat(this.getRightOperand());
    			typeDroite= droiteConv.verifyExpr(compiler, currentClass);
    			droiteConv.setType(typeDroite);
    			this.setRightOperand(droiteConv);
    			
    		}
			else if (!(typeGauche.isClass() && typeDroite.isNull()) && !((typeGauche.isNull() && typeDroite.isClass()))){
				throw new ContextualError("types incompatibles pour faire la comparaison",this.getLocation());
			}
    	}
    	else   //les deux types sont identiques
    	{
    		//si type=booleen,class ou null , la comparaison n'est possible que pour "="ou"!="
    		if((typeGauche.isBoolean()&&typeDroite.isBoolean())||((typeGauche.isClassOrNull()&&typeDroite.isClassOrNull())))
    		{
    			if(!((Objects.equals(this.getOperatorName(),"==")||(Objects.equals(this.getOperatorName(),"!=")))))
    			{
    				throw new ContextualError("types incompatibles pour faire la comparaison",this.getLocation());
    			}
    		}
    		
    	}
    	//le type de retour est boolean 
    	//la méthode n'est pas encore défini !!! il dans passer par DecacCompiler.java le rep deca!!
    	Type typeRetour = new BooleanType(compiler.getSymbolTable().create("boolean"));
        this.setType(typeRetour);
        return typeRetour;
    	
    }
    
    public void codeGenExpr(DecacCompiler compiler, GPRegister op) {
    	DVal rightDval = DValGetter.getDVal(getRightOperand(), compiler);
    	int numeroRegistre = op.getNumber();
    	if(rightDval != null) {
    		getLeftOperand().codeGenExpr(compiler, op);
    		compiler.addInstruction(new CMP(rightDval, op));
    		compiler.addInstruction(this.getMnemo(op));
    	}else {
    		if(numeroRegistre == compiler.getNombreRegistres() - 1) {
    			getLeftOperand().codeGenExpr(compiler, op);
    			compiler.addInstruction(new PUSH(op));
    			compiler.incrementTempPile();
    			getRightOperand().codeGenExpr(compiler, op);
    			compiler.addInstruction(new LOAD(op, Register.R0));
    			compiler.addInstruction(new POP(op));
    			compiler.decrementTempPile();
    			compiler.addInstruction(new CMP(Register.R0, op));
    			compiler.addInstruction(this.getMnemo(op));
    			
    		} else if(numeroRegistre < compiler.getNombreRegistres() - 1) {
    			try {
    				GPRegister nextOp = compiler.getRegisterStart();
        			getLeftOperand().codeGenExpr(compiler, op);
            		getRightOperand().codeGenExpr(compiler, nextOp);
            		compiler.addInstruction(new CMP(nextOp, op));
            		compiler.freeRegister(nextOp);
            		compiler.addInstruction(this.getMnemo(op));
    			} catch (RegisterException e) {
    				getLeftOperand().codeGenExpr(compiler, op);
        			compiler.addInstruction(new PUSH(op));
        			compiler.incrementTempPile();
        			getRightOperand().codeGenExpr(compiler, op);
        			compiler.addInstruction(new LOAD(op, Register.R0));
        			compiler.addInstruction(new POP(op));
        			compiler.decrementTempPile();
        			compiler.addInstruction(new CMP(Register.R0, op));
        			compiler.addInstruction(this.getMnemo(op));
    			}
    		}
    	}
    }
    
    @Override
	protected void codeGenSaut(DecacCompiler compiler, boolean eval, Label etiquette, GPRegister op) {
    	DVal rightDval = DValGetter.getDVal(getRightOperand(), compiler);
    	int numeroRegistre = op.getNumber();
    	Instruction sautInstr = eval ? this.getSaut(etiquette) : this.getNotSaut(etiquette);
    	if(rightDval != null) {
    		getLeftOperand().codeGenExpr(compiler, op);
    		compiler.addInstruction(new CMP(rightDval, op));
    		compiler.addInstruction(sautInstr);
    	}else {
    		if(numeroRegistre == compiler.getNombreRegistres() - 1) {
    			getLeftOperand().codeGenExpr(compiler, op);
    			compiler.addInstruction(new PUSH(op));
    			compiler.incrementTempPile();
    			getRightOperand().codeGenExpr(compiler, op);
    			compiler.addInstruction(new LOAD(op, Register.R0));
    			compiler.addInstruction(new POP(op));
    			compiler.decrementTempPile();
    			compiler.addInstruction(new CMP(Register.R0, op));
    			compiler.addInstruction(sautInstr);
    			
    		} else if(numeroRegistre < compiler.getNombreRegistres() - 1) {
    			try {
    				GPRegister nextOp = compiler.getRegisterStart();
        			getLeftOperand().codeGenExpr(compiler, op);
            		getRightOperand().codeGenExpr(compiler, nextOp);
            		compiler.addInstruction(new CMP(nextOp, op));
            		compiler.freeRegister(nextOp);
            		compiler.addInstruction(sautInstr);
    			}catch(RegisterException e) {
    				getLeftOperand().codeGenExpr(compiler, op);
        			compiler.addInstruction(new PUSH(op));
        			compiler.incrementTempPile();
        			getRightOperand().codeGenExpr(compiler, op);
        			compiler.addInstruction(new LOAD(op, Register.R0));
        			compiler.addInstruction(new POP(op));
        			compiler.decrementTempPile();
        			compiler.addInstruction(new CMP(Register.R0, op));
        			compiler.addInstruction(sautInstr);
    			}
    		}
    	}
	}

	protected abstract Instruction getMnemo(GPRegister op);
    
    /*
     * Renvoie l'instruction du saut associé à l'expression booléenne
     */
    protected abstract Instruction getSaut(Label etiquette);
    
    /*
     * Renvoie le saut inverse associé à l'expression booléenne
     */
    protected abstract Instruction getNotSaut(Label etiquette);
    
}
