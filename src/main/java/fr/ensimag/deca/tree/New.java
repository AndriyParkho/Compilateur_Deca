package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;

public class New extends AbstractExpr{
	
	private AbstractIdentifier identifier;
	
	public New(AbstractIdentifier ident)
	{
		Validate.notNull(ident);
		this.identifier=ident;
	}

	@Override
	public Type verifyExpr(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
		// FAIT
		//on vérifie que le type est bien une classe et on le fixe aprés
		Definition TypeDef=compiler.getEnvTypes().get(compiler.getSymbolTable().create(identifier.getName().toString()));
		if(TypeDef==null)
		{
			throw new ContextualError("type introuvble pour faire la déclaration",this.getLocation());
		}
		else if(!TypeDef.isClass())
		{
			throw new ContextualError("new déclare les classes uniquement",this.getLocation());
		}
		Type type=TypeDef.getType();
		this.setType(type);
		return type;
		
	}

	@Override
	protected void codeGenExpr(DecacCompiler compiler, GPRegister op) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void decompile(IndentPrintStream s) {
		s.print("new ");
		this.identifier.decompile(s);
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		this.identifier.prettyPrint(s,prefix,true);
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

	
	
}