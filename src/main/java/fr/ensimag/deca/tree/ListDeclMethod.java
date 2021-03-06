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
		int index;
		compiler.setEnvExp(currentClass.getMembers());
		for(AbstractDeclMethod method : this.getList())
		{
			index = currentClass.incNumberOfMethods()+1;
			method.verifyMethodMembers(compiler, currentClass, index);
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
    
    public void codeGenListMethod(DecacCompiler compiler, String nomClasse) {
    	  for(AbstractDeclMethod abstractMethod : this.getList()) {
    		  abstractMethod.codeGenMethod(compiler, nomClasse);
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
