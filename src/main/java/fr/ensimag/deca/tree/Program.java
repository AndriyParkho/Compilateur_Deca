package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.EnvironmentType;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.instructions.*;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

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
        //throw new UnsupportedOperationException("not yet implemented");
        try {
        	EnvironmentExp localEnvExp = new EnvironmentExp(null);
        	this.passe1(compiler, localEnvExp);
        	this.passe2(compiler, localEnvExp);
        	this.passe3(compiler, localEnvExp);
        }catch (ContextualError ce) {throw ce;}
        LOG.debug("verify program: end");
    }

    @Override
    public void codeGenProgram(DecacCompiler compiler) {
        // A FAIRE: compléter ce squelette très rudimentaire de code
        compiler.addComment("Main program");
        main.codeGenMain(compiler);
        compiler.addInstruction(new HALT());
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
    
    public void passe1(DecacCompiler compiler, EnvironmentExp localEnvExp) throws ContextualError{}
    
    public void passe2(DecacCompiler compiler, EnvironmentExp localEnvExp) throws ContextualError{}
    
    public void passe3(DecacCompiler compiler, EnvironmentExp localEnvExp) throws ContextualError{
    	try {
    		main.verifyMain(compiler, localEnvExp);
    	}catch(ContextualError ce) {throw ce;}
    	/*this.iter(new TreeFunction() {
            @Override
            public void apply(Tree t) {
                if (t instanceof Identifier) {
                	((Identifier) t).setDefinition(localEnvExp.get(((Identifier) t).getName()));
                }
                else if(t instanceof AbstractExpr) {
                	try {
                		((AbstractExpr) t).setType(((AbstractExpr) t).verifyExpr(compiler, localEnvExp, currentClass));
                	}
                	catch (ContextualError e) {}
                }
            }
        });*/
    }
}
