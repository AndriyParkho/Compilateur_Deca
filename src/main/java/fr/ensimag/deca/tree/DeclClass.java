package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author gl10
 * @date 01/01/2021
 */
public class DeclClass extends AbstractDeclClass {
	
	private AbstractIdentifier name;
	private AbstractIdentifier superClass;
	private ListDeclMethod methodList;
	private ListDeclField fieldList;
	
	
	public DeclClass(AbstractIdentifier name , AbstractIdentifier superClass , ListDeclMethod methodList , ListDeclField fieldList)
	{
		Validate.notNull(name);
		this.name=name;
		this.superClass=superClass;
		this.methodList=methodList;
		this.fieldList=fieldList;
		
	}
	

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("class { ... A FAIRE ... }");
    }

    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }
    
    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
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
