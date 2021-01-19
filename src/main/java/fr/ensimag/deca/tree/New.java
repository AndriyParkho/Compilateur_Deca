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
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.NEW;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.STORE;

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
		// A FAIRE : Auto-generated method stub
		compiler.addInstruction(new NEW(identifier.getClassDefinition().getNumberOfFields() + 1, op));
		//compiler.addInstruction(new BOV(tasPleinLabel));
		compiler.addInstruction(new LEA(identifier.getClassDefinition().getOperand(), GPRegister.R0));
		compiler.addInstruction(new STORE(GPRegister.R0, new RegisterOffset(0, op)));
		compiler.addInstruction(new PUSH(op));
		compiler.addInstruction(new BSR(identifier.getClassDefinition().getInitLabel()));
		compiler.addInstruction(new POP(op));
		compiler.incrCountGB();
		compiler.addInstruction(new STORE(op, new RegisterOffset(compiler.getCountGB(), GPRegister.GB))); // peut etre changer en LB si on le déclare dans une classe, mais vraiment pas sûr
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