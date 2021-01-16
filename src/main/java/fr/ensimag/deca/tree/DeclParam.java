package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * Declaration of a parametre
 *
 * @author lagham
 * @date 14/01/2021
 */
public class DeclParam extends AbstractDeclParam {
	
	private AbstractIdentifier name;
	private AbstractIdentifier type;
	
	public DeclParam(AbstractIdentifier name , AbstractIdentifier type)
	{
		Validate.notNull(name);
		this.name=name;
		this.type=type;
	}
	
	
	@Override
	public  void verifyParamMembers(DecacCompiler compiler , EnvironmentExp lovalEnv , ClassDefinition currentClass)
            throws ContextualError{
		//A FAIRE
	}
	
	@Override
    public void verifyParamBody(DecacCompiler compiler , EnvironmentExp lovalEnv , ClassDefinition currentClass)
            throws ContextualError{
		//A FAIRE
	}
	
	@Override
    public void decompile(IndentPrintStream s) {
		this.type.decompile(s);
		s.print(" ");
		this.name.decompile(s);
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
