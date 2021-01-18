package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.context.*;
import fr.ensimag.ima.pseudocode.GPRegister;
import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * Method call statement.
 *
 * @author gl10
 * @date 18/01/2021
 */
public abstract class AbstractMethodCall extends AbstractExpr{

    private Identifier variable;
    private Identifier method;
    private ListExpr arguments;

    public AbstractMethodCall(Identifier variable, Identifier method, ListExpr arguments){
        Validate.notNull(variable);
        Validate.notNull(method);
        this.variable = variable;
        this.method = method;
        this.arguments = arguments;
    }

    public Identifier getVariable(){ return variable; }
    public Identifier getMethod() { return method; }
    public ListExpr getArguments() { return arguments; }

    @Override
    public Type verifyExpr(DecacCompiler compiler, ClassDefinition currentClass)
                throws ContextualError {
        try {
            variable.verifyExpr(compiler, currentClass);
        }catch (ContextualError ce){throw ce;}
        if (variable.getType().isClass()){
            if (((ClassDefinition) compiler.getEnvTypes().get(variable.getType().getName()))
                    .getMembers().get(method.getName()) == null) {
                throw new ContextualError(String.format("La méthode %s n'existe pas pour la classe %s",
                        method.getName().getName(), variable.getType().getName().getName()), variable.getLocation());
            }
            if ( arguments.getList().size() != method.getMethodDefinition().getSignature().size()){
                throw new ContextualError(String.format("Le nombre d'arguments entrés dans %s ne correspond pas à sa signature",
                        method.getName().getName()),method.getLocation());
            }
            else {
                int index = 0;
                for (AbstractExpr expr : arguments.getList()) {
                    try {
                        expr.verifyExpr(compiler, currentClass);
                    } catch (ContextualError ce) { throw ce; }
                    if (expr.getType() != method.getMethodDefinition().getSignature().paramNumber(index)) {
                        throw new ContextualError(String.format("Argument %d de type %s ne correspond au type du %de argument de la méthode %s",
                                index+1, expr.getType().getName().getName(), index+1, method.getName().getName()),
                                expr.getLocation());
                    }
                    index++;
                }
            }
            this.setType(method.getMethodDefinition().getType());
            return this.getType();
        }
        else{
            throw new ContextualError(String.format("%s n'est pas une classe", variable.getName().getName()),
                    variable.getLocation());
        }
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) throws jumpException{
        //A FAIRE
    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler, GPRegister op) {
        //A FAIRE
    }

}
