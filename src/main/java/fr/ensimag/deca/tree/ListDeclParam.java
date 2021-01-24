package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.util.Iterator;

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
			param.verifyParamMembers(compiler,  lovalEnv ,currentClass);
		}
	}
	
	
    public void verifyParamBody(DecacCompiler compiler , EnvironmentExp lovalEnv , ClassDefinition currentClass)
            throws ContextualError{
		//FAIT
		int index = 1;
    	for(AbstractDeclParam param : this.getList())
    	{
    		param.verifyParamBody(compiler,  lovalEnv , currentClass);
    		param.setIndex(index);
    		index++;
    	}
	}
	
	
	 @Override
	    public void decompile(IndentPrintStream s) {
			Iterator<AbstractDeclParam> iterateur = this.iterator();
			while(iterateur.hasNext()) {
				iterateur.next().decompile(s);				
			}
	 }

}
