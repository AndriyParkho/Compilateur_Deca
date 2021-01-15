package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * Declaration of a field
 *
 * @author lagham
 * @date 14/01/2021
 */
public class DeclField extends AbstractDeclField {
	
	private AbstractIdentifier name;
	private AbstractIdentifier type;
	private AbstractInitialization initialization;
	private Visibility visib;
	
	public DeclField(AbstractIdentifier name , AbstractIdentifier type , AbstractInitialization initialization , Visibility visib)
	{
		this.name=name;
		this.type=type;
		this.initialization=initialization;
		this.visib=visib;
	}
	
	
	@Override
	public  void verifyFieldMembers(DecacCompiler compiler , EnvironmentExp lovalEnv , ClassDefinition currentClass)
            throws ContextualError{
		//A FAIRE
	}
	
	@Override
    public void verifyFieldBody(DecacCompiler compiler , EnvironmentExp lovalEnv , ClassDefinition currentClass)
            throws ContextualError{
		//A FAIRE
	}
	

	@Override
    public void decompile(IndentPrintStream s) {
        s.print("class { ... A FAIRE ... }");
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
