package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;

/**
 *
 * @author gl10
 * @date 01/01/2021
 */
public class While extends AbstractInst {
    private AbstractExpr condition;
    private ListInst body;

    public AbstractExpr getCondition() {
        return condition;
    }

    public ListInst getBody() {
        return body;
    }

    public While(AbstractExpr condition, ListInst body) {
        Validate.notNull(condition);
        Validate.notNull(body);
        this.condition = condition;
        this.body = body;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
    	Label finWhile = new Label("finWhile."+condition.getLocation().getLine()+"."+condition.getLocation().getPositionInLine());
    	Label debutWhile = new Label ("debutWhile"+condition.getLocation().getLine()+"."+condition.getLocation().getPositionInLine());
        compiler.addInstruction(new BRA(finWhile));
    	compiler.addLabel(debutWhile);
        body.codeGenListInst(compiler);
        compiler.addLabel(finWhile);
        GPRegister r = compiler.getRegisterStart();
        condition.codeGenSaut(compiler, true, debutWhile, r);
        compiler.freeRegister(r);
    }

    @Override
    protected void verifyInst(DecacCompiler compiler,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
    	//Fait
    	this.condition.verifyCondition(compiler, currentClass);
    	this.body.verifyListInst(compiler, currentClass, returnType);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("while (");
        getCondition().decompile(s);
        s.println(") {");
        s.indent();
        getBody().decompile(s);
        s.unindent();
        s.print("}");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        condition.iter(f);
        body.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        body.prettyPrint(s, prefix, true);
    }

}
