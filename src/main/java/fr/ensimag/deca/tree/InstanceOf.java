package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CompilerInstruction;
import fr.ensimag.deca.codegen.DValGetter;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.SEQ;

public class InstanceOf extends AbstractExpr{
	
	private AbstractIdentifier type;
	private AbstractExpr objet;
	
	public InstanceOf(AbstractIdentifier type, AbstractExpr objet) {
		this.type=type;
		this.objet=objet;
	}

	@Override
	public Type verifyExpr(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
		// FAIT
		//on vérifie les deux champs et on fixe le type à boolean

		this.type.verifyType(compiler);
		this.objet.verifyExpr(compiler, currentClass);
		try {
			if (!this.type.getType().isClass()){
				throw new ContextualError(String.format("%s n'est pas une instance de classe", this.type.getType().getName().getName()),
						this.getLocation());
			}
			else if (!this.objet.getType().isClass()) {
				throw new ContextualError(String.format("%s n'est pas un objet deca", this.objet.getType().getName().getName()),
						this.getLocation());
			}
		}catch(ContextualError ce){throw ce;}
		Type typeBool=compiler.getEnvTypes().get(compiler.getSymbolTable().create("boolean")).getType();
		this.setType(typeBool);
		return typeBool;
		
	}

	@Override
	public void codeGenExpr(DecacCompiler compiler, GPRegister op) {
		Label fauxLbl = compiler.createLabel("InsatanceOfFaux_" + this.getLocation().getLine() + "_" + this.getLocation().getPositionInLine());
		this.codeGenSaut(compiler, false, fauxLbl, op);
		compiler.addInstruction(new LOAD(1, op));
		Label finInstanceOf = compiler.createLabel("FinInstanceOf_" + this.getLocation().getLine() + "_" + this.getLocation().getPositionInLine());
    	compiler.addInstruction(new BRA(finInstanceOf));
    	
    	compiler.addLabel(fauxLbl);
    	compiler.addInstruction(new LOAD(0, op));
    	
    	compiler.addLabel(finInstanceOf);
	}
	


	@Override
	protected void codeGenSaut(DecacCompiler compiler, boolean eval, Label etiquette, GPRegister op) {
		DVal objetDVal = DValGetter.getDVal(objet, compiler);
		if(objetDVal == null) {
			objet.codeGenExpr(compiler, op);
		} else {
			compiler.addInstruction(new LOAD(objetDVal, op));
		}
		Label finInstanceOf = compiler.createLabel("finInstanceOf." + getLocation().getLine() + "."+getLocation().getPositionInLine());
    	int numeroRegistre = op.getNumber();
    	Instruction sautInstr = eval ? new BEQ(etiquette) : new BEQ(finInstanceOf);
    	ClassType objetType;
    	ClassDefinition currentObjetClass;
    	ClassDefinition typeClass;
    	try {
	    	objetType = (ClassType)objet.getType();
	    	currentObjetClass = (ClassDefinition)objetType.getDefinition();
    	} catch(Exception e) {
    		throw new UnsupportedOperationException("Instance of ne s'applique pas sur les types : " + objet.getType().getName().getName());
    	}
    	try {
    		typeClass = type.getClassDefinition();
    	} catch(Exception e) {
    		throw new UnsupportedOperationException("Instance of ne s'applique pas sur les types : " + type.getType().getName().getName());
    	}
		if(numeroRegistre == compiler.getNombreRegistres() - 1) {
			// ON vérifie si l'objet n'est pas null
			compiler.addInstruction(new CMP(new NullOperand(), op));
			CompilerInstruction.codeGenErreur(compiler, new BEQ(CompilerInstruction.createErreurLabel(compiler, "deferencement.null", "Erreur : deferencement de null")));
			
			if(objetDVal != null) compiler.addInstruction(new LOAD(new RegisterOffset(0, op), op));
			
			while(currentObjetClass != null) {
				compiler.addInstruction(new LEA(typeClass.getOperand(), op));
				compiler.addInstruction(new PUSH(op));
				compiler.incrementTempPile();
				compiler.addInstruction(new LEA(currentObjetClass.getOperand(), Register.R0));
				compiler.addInstruction(new POP(op));
				compiler.decrementTempPile();
				compiler.addInstruction(new CMP(Register.R0, op));
				compiler.addInstruction(sautInstr);
				currentObjetClass = currentObjetClass.getSuperClass();
			}
			
		} else if(numeroRegistre < compiler.getNombreRegistres() - 1) {
			try {
				GPRegister nextOp = compiler.getRegisterStart();
				compiler.addInstruction(new LEA(typeClass.getOperand(), nextOp));
				compiler.addInstruction(new CMP(new NullOperand(), op));
				CompilerInstruction.codeGenErreur(compiler, new BEQ(CompilerInstruction.createErreurLabel(compiler, "deferencement.null", "Erreur : deferencement de null")));
				
				if(objetDVal != null) compiler.addInstruction(new LOAD(new RegisterOffset(0, op), op));
				
				compiler.addInstruction(new CMP(nextOp, op));
				compiler.addInstruction(sautInstr);
				currentObjetClass = currentObjetClass.getSuperClass();
				
				while(currentObjetClass != null) {
					compiler.addInstruction(new LEA(currentObjetClass.getOperand(), op));
					compiler.addInstruction(new CMP(nextOp, op));
					compiler.addInstruction(sautInstr);
					currentObjetClass = currentObjetClass.getSuperClass();
				}
				compiler.freeRegister(nextOp);
			} catch(RegisterException e) {
				compiler.addInstruction(new CMP(new NullOperand(), op));
				CompilerInstruction.codeGenErreur(compiler, new BEQ(CompilerInstruction.createErreurLabel(compiler, "deferencement.null", "Erreur : deferencement de null")));
				
				if(objetDVal != null) compiler.addInstruction(new LOAD(new RegisterOffset(0, op), op));
				
				while(currentObjetClass != null) {
					compiler.addInstruction(new LEA(typeClass.getOperand(), op));
					compiler.addInstruction(new PUSH(op));
					compiler.incrementTempPile();
					compiler.addInstruction(new LEA(currentObjetClass.getOperand(), Register.R0));
					compiler.addInstruction(new POP(op));
					compiler.decrementTempPile();
					compiler.addInstruction(new CMP(Register.R0, op));
					compiler.addInstruction(sautInstr);
					currentObjetClass = currentObjetClass.getSuperClass();
				}
			}
		}
		if(!eval) {
			compiler.addInstruction(new BRA(etiquette));
			compiler.addLabel(finInstanceOf);
		}
	}

	@Override
	public void decompile(IndentPrintStream s) {
		this.objet.decompile(s);
		s.print(" instanceof ");
		this.type.decompile(s);
		
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		this.objet.prettyPrint(s, prefix, false);
        this.type.prettyPrint(s, prefix, true);
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		// TODO Auto-generated method stub
		
	}
	
}