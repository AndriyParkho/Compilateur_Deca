package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;

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
		Type typeBool=compiler.getEnvTypes().get(compiler.getSymbolTable().create("boolean")).getType();
		this.setType(typeBool);
		return typeBool;
		
	}

	@Override
	protected void codeGenExpr(DecacCompiler compiler, GPRegister op) {
		throw new jumpException("La fonction codeGenExpr n'est pas implémentée pour l'expression : InstanceOf");
	}
	


	@Override
	protected void codeGenSaut(DecacCompiler compiler, boolean eval, Label etiquette, GPRegister op) {
		// A FAIRE
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
	
}