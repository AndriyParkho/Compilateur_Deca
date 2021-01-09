package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.EnvironmentType;

/**
 * Entry point for contextual verifications and code generation from outside the package.
 * 
 * @author gl10
 * @date 01/01/2021
 *
 */
public abstract class AbstractProgram extends Tree {
	
    public abstract void verifyProgram(DecacCompiler compiler) throws ContextualError;
    public abstract void codeGenProgram(DecacCompiler compiler) ;

}
