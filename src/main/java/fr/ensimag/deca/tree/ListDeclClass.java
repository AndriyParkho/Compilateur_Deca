package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.InitObjectClass;
import fr.ensimag.deca.codegen.CompilerInstruction;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

import org.apache.log4j.Logger;

/**
 *
 * @author gl10
 * @date 01/01/2021
 */
public class ListDeclClass extends TreeList<AbstractDeclClass> {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);
    
    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclClass c : getList()) {
            c.decompile(s);
            s.println();
        }
    }

    /**
     * Pass 1 of [SyntaxeContextuelle]
     */
    void verifyListClass(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify listClass: start");
        //FAIT
        for(AbstractDeclClass classe : this.getList())
        {
        	classe.verifyClass(compiler);
        }
        
        LOG.debug("verify listClass: end");
    }

    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    public void verifyListClassMembers(DecacCompiler compiler) throws ContextualError {
    	
        //FAIT
    	for(AbstractDeclClass classe : this.getList())
    	{
    		classe.verifyClassMembers(compiler);
    	}
    }
    
    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    public void verifyListClassBody(DecacCompiler compiler) throws ContextualError {
    	//FAIT
        for(AbstractDeclClass classe : this.getList())
        {
        	classe.verifyClassBody(compiler);
        }
    	
    }
    
    public void codeGenListClassMethodTable(DecacCompiler compiler) {
    	// A FAIRE : 
    	// Initialisation de la classe Object
    	CompilerInstruction.decorationAssembleur(compiler, "Construction des tables des méthodes");
    	InitObjectClass.initObjectMethodsTbl(compiler);
    	
    	for(AbstractDeclClass classe : this.getList()) {
    		classe.codeGenClassMethodTable(compiler);
    	}
    }
    
    public void codeGenListClassBody(DecacCompiler compiler) {
    	

    	InitObjectClass.codeGenEqualsMethod(compiler);
    	for(AbstractDeclClass classe : getList()) {
    		classe.codeGenClassBody(compiler);
    	}
    }

}
