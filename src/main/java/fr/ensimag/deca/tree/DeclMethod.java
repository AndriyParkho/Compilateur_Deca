package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * Declaration of a method
 *
 * @author lagham
 * @date 14/01/2021
 */
public class DeclMethod extends AbstractDeclMethod {
	
	private AbstractIdentifier name;
	private AbstractIdentifier type;
	private ListDeclParam paramList;
	private AbstractMethodBody methodBody;
	
	public DeclMethod(AbstractIdentifier name , AbstractIdentifier type , ListDeclParam paramList , AbstractMethodBody methodBody)
	{
		Validate.notNull(name);
		this.name=name;
		this.type=type;
		this.paramList=paramList;
		this.methodBody=methodBody;
	}

	
	@Override
	public  void verifyMethodMembers(DecacCompiler compiler  , ClassDefinition currentClass)
            throws ContextualError{
		//A FAIRE
	}
	
	@Override
    public void verifyMethodBody(DecacCompiler compiler  , ClassDefinition currentClass)
            throws ContextualError{
		//A FAIRE
	}
	
	@Override
    public void decompile(IndentPrintStream s) {
		this.type.decompile(s);
		s.print(" ");
		this.name.decompile(s);
		s.print("(");
		this.paramList.decompile(s);
		s.print(") {");
		this.methodBody.decompile(s);
		s.print(")");
    }
	
	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
	        throw new UnsupportedOperationException("Not yet supported");
	    }

	@Override
	protected void iterChildren(TreeFunction f) {
	        throw new UnsupportedOperationException("Not yet supported");
	    }
}
