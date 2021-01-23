package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.NullType;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

public class Null extends AbstractExpr{

	@Override
	public Type verifyExpr(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
		//Fait
		//il suffit de set le type à null
		Type type=new NullType(compiler.getSymbolTable().create("null"));
		this.setType(type);
		return type;
	}

	@Override
	public void codeGenExpr(DecacCompiler compiler, GPRegister op) {
		compiler.addInstruction(new LOAD(new NullOperand(), op));
		
	}

	@Override
	public void decompile(IndentPrintStream s) {
		s.print("null");
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		s.print(prefix);
        s.print("null");
        s.println();
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean isNull() {
		return true;
	}
	
}