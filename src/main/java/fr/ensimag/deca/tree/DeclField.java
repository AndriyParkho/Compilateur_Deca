package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * Declaration of a field
 *
 * @author lagham
 * @date 14/01/2021
 */
public class DeclField extends AbstractDeclField {
	

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
