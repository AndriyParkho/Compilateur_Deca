package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Method declaration.
 *
 * @author lagham
 * @date 14/01/2021
 */
public abstract class AbstractDeclMethod extends Tree {
	
	public abstract void verifyMethodMembers(DecacCompiler compiler  , ClassDefinition currentClass, int index)
            throws ContextualError;
    public abstract void verifyMethodBody(DecacCompiler compiler  , ClassDefinition currentClass)
            throws ContextualError;
    
    public abstract void setLabel(String className);
    
    public abstract void codeGenMethod(DecacCompiler compiler);

}
