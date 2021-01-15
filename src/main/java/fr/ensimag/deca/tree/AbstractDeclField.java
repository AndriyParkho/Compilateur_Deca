package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Field declaration.
 *
 * @author lagham
 * @date 14/01/2021
 */
public abstract class AbstractDeclField extends Tree {

	public abstract void verifyFieldMembers(DecacCompiler compiler , EnvironmentExp lovalEnv , ClassDefinition currentClass)
	                     throws ContextualError;
	public abstract void verifyFieldBody(DecacCompiler compiler , EnvironmentExp lovalEnv , ClassDefinition currentClass)
            throws ContextualError;

}
