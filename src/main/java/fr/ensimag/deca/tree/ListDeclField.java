package fr.ensimag.deca.tree;

import java.util.Iterator;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
//import org.apache.log4j.Logger;

/**
 *
 * @author lagham
 * @date 14/01/2021
 */
public class ListDeclField extends TreeList<AbstractDeclField> {
	
	
	public  void verifyFieldMembers(DecacCompiler compiler , EnvironmentExp lovalEnv , ClassDefinition currentClass)
            throws ContextualError{
		//A FAIRE
	}
	
	
    public void verifyFieldBody(DecacCompiler compiler , EnvironmentExp lovalEnv , ClassDefinition currentClass)
            throws ContextualError{
		//A FAIRE
	}
	
	@Override
    public void decompile(IndentPrintStream s) {
		Iterator<AbstractDeclField> iterateur = this.iterator();
        while(iterateur.hasNext()) {
            iterateur.next().decompile(s);    
        }
 }


}
