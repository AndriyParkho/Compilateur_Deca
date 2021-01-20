package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;

/**
 * Class declaration.
 *
 * @author lagham
 * @date 14/01/2021
 */
public abstract class AbstractMethodBody extends Tree {
	private Type returnType;
	
	public Type getReturnType() {return returnType;}
	public void setType(Type type) {returnType=type;}
	
	public abstract void verifyMethodBody(DecacCompiler compiler, ClassDefinition currentClass, Type returnType)
	    throws ContextualError;
	
	public abstract void codeGenMethodBody(DecacCompiler compiler);
  
}
