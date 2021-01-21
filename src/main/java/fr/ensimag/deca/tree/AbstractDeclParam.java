package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Parametre declaration.
 *
 * @author lagham
 * @date 01/01/2021
 */
public abstract class AbstractDeclParam extends Tree {

    private int index;

    public int getIndex(){return this.index ;}
    public void setIndex(int i){this.index = i;}

	public abstract void verifyParamMembers(DecacCompiler compiler , EnvironmentExp lovalEnv ,ClassDefinition currentClass)
            throws ContextualError;
    public abstract void verifyParamBody(DecacCompiler compiler  ,EnvironmentExp lovalEnv , ClassDefinition currentClass)
            throws ContextualError;
    
    public abstract void codeGenParam(DecacCompiler compiler);


}
