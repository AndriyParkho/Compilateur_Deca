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
		// A FAIRE
    	int numeroRegistre = op.getNumber();
    	ClassType objetType;
    	ClassDefinition currentObjetClass;
    	ClassDefinition typeClass;
    	Instruction equalInst = new SEQ(op);
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
		if(numeroRegistre == compiler.getNombreRegistres()) {
			// ON vérifie si l'objet n'est pas null
			compiler.addInstruction(new LEA(currentObjetClass.getOperand(), op));
			compiler.addInstruction(new CMP(new NullOperand(), op));
			compiler.addInstruction(new BEQ(CompilerInstruction.createErreurLabel(compiler, "deferencement.null", "Erreur : deferencement de null")));
			
			
			while(currentObjetClass != null) {
				compiler.addInstruction(new LEA(typeClass.getOperand(), op));
				compiler.addInstruction(new PUSH(op));
				compiler.incrementTempPile();
				compiler.addInstruction(new LEA(currentObjetClass.getOperand(), Register.R0));
				compiler.addInstruction(new POP(op));
				compiler.decrementTempPile();
				compiler.addInstruction(new CMP(Register.R0, op));
				compiler.addInstruction(equalInst);
				currentObjetClass = currentObjetClass.getSuperClass();
			}
			
		} else if(numeroRegistre < compiler.getNombreRegistres()) {
			GPRegister nextOp = compiler.getRegisterStart();
			compiler.addInstruction(new LEA(typeClass.getOperand(), nextOp));
			compiler.addInstruction(new LEA(currentObjetClass.getOperand(), op));
			compiler.addInstruction(new CMP(new NullOperand(), op));
			compiler.addInstruction(new BEQ(CompilerInstruction.createErreurLabel(compiler, "deferencement.null", "Erreur : deferencement de null")));
			
			compiler.addInstruction(new CMP(nextOp, op));
			compiler.addInstruction(equalInst);
			currentObjetClass = currentObjetClass.getSuperClass();
			
			while(currentObjetClass != null) {
				compiler.addInstruction(new LEA(currentObjetClass.getOperand(), op));
				compiler.addInstruction(new CMP(nextOp, op));
				compiler.addInstruction(equalInst);
				currentObjetClass = currentObjetClass.getSuperClass();
			}
			compiler.freeRegister(nextOp);
		}
	}
	


	@Override
	protected void codeGenSaut(DecacCompiler compiler, boolean eval, Label etiquette, GPRegister op) {
		// A FAIRE
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
		if(numeroRegistre == compiler.getNombreRegistres()) {
			// ON vérifie si l'objet n'est pas null
			compiler.addInstruction(new LEA(currentObjetClass.getOperand(), op));
			compiler.addInstruction(new CMP(new NullOperand(), op));
			compiler.addInstruction(new BEQ(CompilerInstruction.createErreurLabel(compiler, "deferencement.null", "Erreur : deferencement de null")));
			
			
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
			
		} else if(numeroRegistre < compiler.getNombreRegistres()) {
			GPRegister nextOp = compiler.getRegisterStart();
			compiler.addInstruction(new LEA(typeClass.getOperand(), nextOp));
			compiler.addInstruction(new LEA(currentObjetClass.getOperand(), op));
			compiler.addInstruction(new CMP(new NullOperand(), op));
			compiler.addInstruction(new BEQ(CompilerInstruction.createErreurLabel(compiler, "deferencement.null", "Erreur : deferencement de null")));
			
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

	@Override
	public boolean isIntLiteral() {
		//!
		return false;
	}

	@Override
	public boolean isFloatLiteral() {
		// !
		return false;
	}

	@Override
	public boolean isBooleanLiteral() {
		// !
		return false;
	}

	@Override
	public boolean isIdentifier() {
		// !
		return false;
	}

	@Override
	public boolean isDot() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isMethod() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isThis() {
		// TODO Auto-generated method stub
		return false;
	}
	
}