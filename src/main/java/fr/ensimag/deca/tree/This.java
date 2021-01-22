package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

public class This extends AbstractExpr{

	@Override
	public Type verifyExpr(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
		//Fait
		//on fait appel à this que dans une classe
		if(currentClass == null)
		{
			throw new ContextualError("utilisation de this non autotrisée en dehors d'une classe",this.getLocation());
		}
		else
		{
			Type type=currentClass.getType();
			this.setType(type);
			return type;
		}
	}

	@Override
	public void codeGenExpr(DecacCompiler compiler, GPRegister op) {
		///A FAIRE : mettre dans R2 l'adresse de l'objet
		compiler.addInstruction(new LOAD(new RegisterOffset(2, GPRegister.LB), op));
		
	}

	@Override
	public void decompile(IndentPrintStream s) {
		s.print("this");
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		s.print(prefix);
        s.println();
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isIntLiteral() {
		// !
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
		return false;
	}

	@Override
	public boolean isMethod() {
		return false;
	}

	@Override
	public boolean isThis() {
		return true;
	}
	
}