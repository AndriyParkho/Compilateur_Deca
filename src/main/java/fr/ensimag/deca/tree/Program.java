package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.compilerInstruction;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.instructions.HALT;

/**
 * Deca complete program (class definition plus main block)
 *
 * @author gl10
 * @date 01/01/2021
 */
public class Program extends AbstractProgram {
    private static final Logger LOG = Logger.getLogger(Program.class);
    
    public Program(ListDeclClass classes, AbstractMain main) {
        Validate.notNull(classes);
        Validate.notNull(main);
        this.classes = classes;
        this.main = main;
    }
    public ListDeclClass getClasses() {
        return classes;
    }
    public AbstractMain getMain() {
        return main;
    }
    private ListDeclClass classes;
    private AbstractMain main;

    @Override
    public void verifyProgram(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify program: start");
        try {
        	this.passe1(compiler);
        	this.passe2(compiler);
        	this.passe3(compiler);
        }catch (ContextualError ce) {throw ce;}
        LOG.debug("verify program: end");
    }

    @Override
    public void codeGenProgram(DecacCompiler compiler) {
    	classes.codeGenListClassMethodTable(compiler);
        compiler.addComment("Main program");
        main.codeGenMain(compiler);
        compiler.addInstruction(new HALT());
        //A FAIRE : piles + avancées
        compiler.addComment("end main program");
        compilerInstruction.gestionPileVariablesGlobales(compiler);
        compiler.codeGenErrLbl();
    }

    @Override
    public void decompile(IndentPrintStream s) {
        getClasses().decompile(s);
        getMain().decompile(s);
    }
    
    @Override
    protected void iterChildren(TreeFunction f) {
        classes.iter(f);
        main.iter(f);
    }
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        classes.prettyPrint(s, prefix, false);
        main.prettyPrint(s, prefix, true);
    }
    
    public void passe1(DecacCompiler compiler) throws ContextualError{
        try{
            classes.verifyListClass(compiler);
        }catch(ContextualError ce){throw ce;}
    }
    
    public void passe2(DecacCompiler compiler) throws ContextualError{
        try{
            classes.verifyListClassMembers(compiler);
        }catch(ContextualError ce){throw ce;}
    }
    
    public void passe3(DecacCompiler compiler) throws ContextualError{
    	try {
    	    classes.verifyListClassBody(compiler);
    		main.verifyMain(compiler);
    	}catch(ContextualError ce) {throw ce;}
    }
}
