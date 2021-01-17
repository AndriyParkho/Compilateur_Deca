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
public class ListDeclMethod extends TreeList<AbstractDeclMethod> {
	
	
	public  void verifyMethodMembers(DecacCompiler compiler , ClassDefinition currentClass)
            throws ContextualError{
		//FAIT
		for(AbstractDeclMethod method : this.getList())
		{
			method.verifyMethodMembers(compiler, currentClass);
		}
	}
	
	
    public void verifyMethodBody(DecacCompiler compiler  , ClassDefinition currentClass)
            throws ContextualError{
		//Fait
    	for(AbstractDeclMethod method : this.getList())
    	{
    		method.verifyMethodBody(compiler, currentClass);
    	}
	}
	
	@Override
    public void decompile(IndentPrintStream s) {
		Iterator<AbstractDeclMethod> iterateur = this.iterator();
        while(iterateur.hasNext()) {
            iterateur.next().decompile(s);    
        }
 }


}
