package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * Method call statement.
 *
 * @author gl10
 * @date 18/01/2021
 */
public abstract class AbstractMethodCall extends AbstractInst{

    private Identifier variable;
    private Identifier method;
    private ListExpr arguments;

    public AbstractMethodCall(Identifier variable, Identifier method, ListExpr arguments){
        Validate.notNull(variable);
        Validate.notNull(method);
        variable = variable;
        method = method;
        arguments = arguments;
    }

    public Identifier getVariable(){ return variable; }
    public Identifier getMethod() { return method; }
    public ListExpr getArguments() { return arguments; }

    /**
     * Implements non-terminal "inst" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains the "env_types" and the "env_exp" attribute
     * @param currentClass
     *          corresponds to the "class" attribute (null in the main bloc).
     * @param returnType
     *          corresponds to the "return" attribute (void in the main bloc).
     */
    protected void verifyInst(DecacCompiler compiler,
                                       ClassDefinition currentClass, Type returnType) throws ContextualError{
        //A FAIRE
    }

    /**
     * Generate assembly code for the instruction.
     *
     * @param compiler
     * @throws jumpException
     */
    protected void codeGenInst(DecacCompiler compiler) throws jumpException{
        //A FAIRE
    }

}
