package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CompilerInstruction;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.ERROR;
import fr.ensimag.ima.pseudocode.instructions.WNL;
import fr.ensimag.ima.pseudocode.instructions.WSTR;

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
		// FAIT
		//pas de return si returntype=void + type et returntype compatible (float<=int autorisé!)
		
		
		if(returnType.isVoid())
		{
			throw new ContextualError("return non autorisé pour un returnType void",this.getLocation());
		}
		else
		{
			this.returnExpr.verifyRValue(compiler, currentClass, returnType);
			Type type=this.returnExpr.getType();
			if(returnType.isFloat() && type.isInt())
			{
				ConvFloat reel=new ConvFloat(returnExpr);
				reel.verifyExpr(compiler, currentClass);
				reel.setType(returnType);
				this.returnExpr=reel;
			}
			else if(!type.sameType(returnType))
			{
				throw new ContextualError("type incompatible avec le returnType",this.getLocation());
			}
		}
		this.returnExpr.setType(returnType);
	}
	@Override
	protected void codeGenInst(DecacCompiler compiler) throws JumpException {
		returnExpr.codeGenExpr(compiler, GPRegister.R1);
		compiler.addInstruction(new BRA(compiler.createLabel("fin."+CompilerInstruction.currentClassMethodString(compiler))));
		compiler.addInstruction(new WSTR("Erreur : sortie de la méthode "+CompilerInstruction.currentClassMethodString(compiler) + " sans return"));
		compiler.addInstruction(new WNL());
		compiler.addInstruction(new ERROR());
		
	}
	@Override
	public void decompile(IndentPrintStream s) {
		s.print("return ");
		this.returnExpr.decompile(s);
		s.println(";");
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