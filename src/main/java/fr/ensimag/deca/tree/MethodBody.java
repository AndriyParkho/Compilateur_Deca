package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.context.*;
import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 *
 * @author lagham
 * @date 14/01/2021
 */
public class MethodBody extends AbstractMethodBody {
	
	private ListDeclVar listDeclVar;
	private ListInst listInst;
	
	public MethodBody(ListDeclVar listVar, ListInst listInst)
	{
		Validate.notNull(listVar);
		Validate.notNull(listInst);
		this.listDeclVar=listVar;
		this.listInst=listInst;
	}
	
	
	@Override
	public void verifyMethodBody(DecacCompiler compiler, ClassDefinition currentClass, Type returnType)
		    throws ContextualError{
		//FAIT
		this.listDeclVar.verifyListDeclVariable(compiler, currentClass);
		this.listInst.verifyListInst(compiler, currentClass, returnType);
	    this.setType(returnType);
	}
	
	@Override
    public void decompile(IndentPrintStream s) {
		this.listDeclVar.decompile(s);
		this.listInst.decompile(s);
    }
	
	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
			this.listDeclVar.prettyPrint(s, prefix, false);
			this.listInst.prettyPrint(s, prefix, false);
	    }

	@Override
	protected void iterChildren(TreeFunction f) {
	        throw new UnsupportedOperationException("Not yet supported");
	    }
	
	public void codeGenMethodBody(DecacCompiler compiler) {
		listDeclVar.codeGenListDeclVar(compiler);
		listInst.codeGenListInst(compiler);
	}

}
