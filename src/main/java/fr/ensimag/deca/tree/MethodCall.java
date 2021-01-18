package fr.ensimag.deca.tree;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.instructions.WNL;
import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * @author gl10
 * @date 18/01/2021
 */
public class MethodCall extends AbstractMethodCall{

    public MethodCall(Identifier variable, Identifier method, ListExpr arguments){
        super(variable, method, arguments);
    }

    @Override
    public void decompile(IndentPrintStream s) {}

    @Override
    protected void iterChildren(TreeFunction f) {this.getArguments().iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        this.getArguments().prettyPrint(s, prefix, true);
    }
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        //A FAIRE
    }


}
