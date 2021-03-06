package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * Full if/else if/else statement.
 *
 * @author gl10
 * @date 01/01/2021
 */
public class IfThenElse extends AbstractInst {
    
    private final AbstractExpr condition; 
    private final ListInst thenBranch;
    private ListInst elseBranch;

    public IfThenElse(AbstractExpr condition, ListInst thenBranch, ListInst elseBranch) {
        Validate.notNull(condition);
        Validate.notNull(thenBranch);
        Validate.notNull(elseBranch);
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }
    
    @Override
    protected void verifyInst(DecacCompiler compiler,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
    	//Fait
    	this.condition.verifyCondition(compiler, currentClass);
        this.thenBranch.verifyListInst(compiler, currentClass, returnType);
    	this.elseBranch.verifyListInst(compiler, currentClass, returnType);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler)  {
    	Label elseLbl = compiler.createLabel("Else_" + this.getLocation().getLine() + "_" + this.getLocation().getPositionInLine());
    	GPRegister r = compiler.getRegisterStart();
    	condition.codeGenSaut(compiler, false, elseLbl, r);
    	compiler.freeRegister(r);
    	thenBranch.codeGenListInst(compiler);
    	Label finIfLbl = compiler.createLabel("FinIf_" + this.getLocation().getLine() + "_" + this.getLocation().getPositionInLine());
    	compiler.addInstruction(new BRA(finIfLbl));
    	
    	compiler.addLabel(elseLbl);
    	elseBranch.codeGenListInst(compiler);
    	
    	compiler.addLabel(finIfLbl);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("if (");
        condition.decompile(s);
        s.println(") {");
        s.indent();
        thenBranch.decompile(s);
        s.unindent();
        s.println("} else {");
        s.indent();
        elseBranch.decompile(s);
        s.unindent();
        s.print("}");
    }

    public void setListElse(ListInst liste) {
        this.elseBranch = liste;
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        condition.iter(f);
        thenBranch.iter(f);
        elseBranch.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        thenBranch.prettyPrint(s, prefix, false);
        elseBranch.prettyPrint(s, prefix, true);
    }
}
