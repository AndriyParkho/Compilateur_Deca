package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
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
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
    	//Fait
    	this.condition.verifyCondition(compiler, localEnv, currentClass);
        this.setLocation(this.condition.getLocation());
        this.thenBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
    	this.elseBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler)  {
        // A FAIRE : gestion du branchement conditionnel
    	condition.codeGenExpr(compiler, compiler.getRegisterStart());
    	Label labelSaut = condition.codeGenSaut(compiler, "sinonIfLine"+condition.getLocation().getLine()+"Pos"+condition.getLocation().getPositionInLine());
    	elseBranch.codeGenListInst(compiler);
    	Label finIf = new Label("finIfLine"+condition.getLocation().getLine());
    	compiler.addInstruction(new BRA(finIf));
    	compiler.addLabel(labelSaut);
    	thenBranch.codeGenListInst(compiler);
    	compiler.addLabel(finIf);
    	
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("if (");
        condition.decompile();
        s.println(") {");
        thenBranch.decompile();
        s.println("} else {");
        elseBranch.decompile();
        s.println("}");
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
