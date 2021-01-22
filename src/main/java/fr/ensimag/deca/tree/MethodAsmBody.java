package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 *
 *
 * @author lagham
 * @date 14/01/2021
 */
public class MethodAsmBody extends AbstractMethodBody {
	
	private StringLiteral string;
	
	public MethodAsmBody(StringLiteral string)
	{
		this.string=string;
	}
	
	
	@Override
	public void verifyMethodBody(DecacCompiler compiler, ClassDefinition currentClass, Type returnType)
		    throws ContextualError{
		//A FAIRE
	}
	
	@Override
    public void decompile(IndentPrintStream s) {
        s.print("class { ... A FAIRE ... }");
    }
	
	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		s.print(prefix);
        s.print(string.getValue());
        s.println();
	    }

	@Override
	protected void iterChildren(TreeFunction f) {
		string.iter(f);
	}


	@Override
	public void codeGenMethodBody(DecacCompiler compiler) {
		// TODO Auto-generated method stub
		
	}



	

}
