package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.log4j.Logger;

/**
 *
 * @author lagham
 * @date 14/01/2021
 */
public class ListDeclParam extends TreeList<AbstractDeclParam> {
	
	public  void verifyParamMembers(DecacCompiler compiler , EnvironmentExp lovalEnv , ClassDefinition currentClass)
            throws ContextualError{
		//FAIT
		for(AbstractDeclParam param : this.getList())
		{
			param.verifyParamMembers(compiler, currentClass);
		}
	}
	
	
    public void verifyParamBody(DecacCompiler compiler , EnvironmentExp lovalEnv , ClassDefinition currentClass)
            throws ContextualError{
		//FAIT
    	for(AbstractDeclParam param : this.getList())
    	{
    		param.verifyParamBody(compiler, currentClass);
    	}
	}
	
	
	 @Override
	    public void decompile(IndentPrintStream s) {
		 //A FAIRE
	 }

}
