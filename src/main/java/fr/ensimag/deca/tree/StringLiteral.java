package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.StringType;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateString;
import fr.ensimag.ima.pseudocode.instructions.WSTR;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * String literal
 *
 * @author gl10
 * @date 01/01/2021
 */
public class StringLiteral extends AbstractStringLiteral {

    @Override
    public String getValue() {
        return value;
    }

    private String value;

    public StringLiteral(String value) {
        Validate.notNull(value);
        this.value = value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler,
            ClassDefinition currentClass) throws ContextualError {
        //Fait

    	StringType type = new StringType(compiler.getSymbolTable().create("String"));
    	this.setType(type);
    	return type;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("\""+value+"\"");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }
    
    @Override
    String prettyPrintNode() {
        return "StringLiteral (" + value + ")";
    }

    String prettyPrintASM() {
        return "StringLiteral (" + value + ")";
    }

	@Override
	public void codeGenExpr(DecacCompiler compiler, GPRegister op) {
		// FAIT : Rien à faire
	}
	
}
