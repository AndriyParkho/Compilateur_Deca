package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;

public class InstanceOf extends AbstractExpr{
	
	private AbstractIdentifier type;
	private AbstractExpr objet;
	
	public InstanceOf(AbstractIdentifier type, AbstractExpr objet) {
		this.type=type;
		this.objet=objet;
	}

	@Override
	public Type verifyExpr(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void codeGenExpr(DecacCompiler compiler, GPRegister op) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isFloatLiteral() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isBooleanLiteral() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isIdentifier() {
		// TODO Auto-generated method stub
		return false;
	}
	
}