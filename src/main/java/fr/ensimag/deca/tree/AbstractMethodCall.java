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

    private AbstractExpr variable;
    private AbstractIdentifier method;
    private ListExpr arguments;

    public AbstractMethodCall(AbstractExpr variable, AbstractIdentifier method, ListExpr arguments){
        Validate.notNull(variable);
        Validate.notNull(method);
        this.variable = variable;
        this.method = method;
        this.arguments = arguments;
    }

    public AbstractExpr getVariable(){ return variable; }
    public AbstractIdentifier getMethod() { return method; }
    public ListExpr getArguments() { return arguments; }

    @Override
    public Type verifyExpr(DecacCompiler compiler, ClassDefinition currentClass)
                throws ContextualError {
        EnvironmentExp envLocal = compiler.getEnvExp();
        try {
            variable.verifyExpr(compiler, currentClass);
            if (!variable.getType().isClass()){
                throw new ContextualError("l'appel de méthode est utilisable uniquement pour les instances de classe", this.getLocation());
            }
            compiler.setEnvExp(((ClassDefinition)compiler.getEnvTypes().get(compiler.getSymbolTable().create(variable.getType().getName().getName()))).getMembers());
            method.verifyExpr(compiler, currentClass);
            compiler.setEnvExp(envLocal);
        }catch (ContextualError ce){throw ce;}
        if (variable.getType().isClass()){
            if (((ClassDefinition) compiler.getEnvTypes().get(compiler.getSymbolTable().create(variable.getType().getName().getName())))
                    .getMembers().get(compiler.getSymbolTable().create(method.getName().getName())) == null &&
                    ((ClassDefinition) compiler.getEnvTypes().get(compiler.getSymbolTable().create(variable.getType().getName().getName())))
                            .getMembers().get(method.getName()) == null) {
                throw new ContextualError(String.format("La méthode %s n'existe pas pour la classe %s",
                        method.getName().getName(), variable.getType().getName().getName()), variable.getLocation());
            }
            if (arguments.getList().size() != method.getMethodDefinition().getSignature().size()){
                throw new ContextualError(String.format("Le nombre d'arguments entrés dans %s ne correspond pas à sa signature",
                        method.getName().getName()),method.getLocation());
            }
            else {
                int index = 0;
                for (AbstractExpr expr : arguments.getList()) {
                    try {
                        expr.verifyExpr(compiler, currentClass);
                    } catch (ContextualError ce) { throw ce; }
                    if (expr.getType().getName() != method.getMethodDefinition().getSignature().paramNumber(index).getName()) {
                        if (expr.getType().isInt() && method.getMethodDefinition().getSignature().paramNumber(index).isFloat()){
                            ConvFloat nouvelArgument = new ConvFloat(expr);
                            expr = nouvelArgument;
                        }
                        else if (method.getMethodDefinition().getSignature().paramNumber(index).isClass()){
                           try{
                               if (expr.getType().isNull()){}
                               else if (!expr.getType().isClass()){
                                   throw new ContextualError(String.format("Argument %d de type %s ne correspond au type du %de argument de la méthode %s",
                                           index + 1, expr.getType().getName().getName(), index + 1, method.getName().getName()),
                                           expr.getLocation());
                               }
                               else if (!((ClassType)expr.getType()).isSubClassOf((ClassType) method.getMethodDefinition().getSignature().paramNumber(index))){
                                   throw new ContextualError(String.format("%s ne peut pas être cast en %s", expr.getType(), method.getMethodDefinition().getSignature().paramNumber(index).getName()), this.getLocation());
                               }
                           }catch (ContextualError ce){throw ce;}
                        }
                        else {
                            throw new ContextualError(String.format("Argument %d de type %s ne correspond au type du %de argument de la méthode %s",
                                    index + 1, expr.getType().getName().getName(), index + 1, method.getName().getName()),
                                    expr.getLocation());
                        }
                    }
                    index++;
                }
            }
            this.setType(method.getMethodDefinition().getType());
            return this.getType();
        }
        else{
            throw new ContextualError(String.format("%s n'est pas un objet", variable.getType().getName().getName()),
                    variable.getLocation());
        }
    }
}
