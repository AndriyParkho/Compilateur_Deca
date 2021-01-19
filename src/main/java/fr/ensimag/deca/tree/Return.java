package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

public class Return extends AbstractInst{
	
	private AbstractExpr returnExpr;
	public Return(AbstractExpr expr)
	{
		this.returnExpr=expr;
	}
	public AbstractExpr getReturnExpr()
	{
		return returnExpr;
	}
	@Override
	protected void verifyInst(DecacCompiler compiler, ClassDefinition currentClass, Type returnType)
			throws ContextualError {
		// TODO 
		
	}
	@Override
	protected void codeGenInst(DecacCompiler compiler) throws jumpException {
		// TODO 
		
	}
	@Override
	public void decompile(IndentPrintStream s) {
		s.print("return ");
		this.returnExpr.decompile(s);
	}
	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		this.returnExpr.prettyPrint(s,prefix,true);
	}
	@Override
	protected void iterChildren(TreeFunction f) {
		// TODO 
		
	}
	
}