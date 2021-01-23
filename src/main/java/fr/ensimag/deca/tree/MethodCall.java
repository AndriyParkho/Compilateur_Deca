package fr.ensimag.deca.tree;
import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CompilerInstruction;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.SUBSP;

/**
 * @author gl10
 * @date 18/01/2021
 */
public class MethodCall extends AbstractMethodCall{

    public MethodCall(AbstractExpr variable, AbstractIdentifier method, ListExpr arguments){
        super(variable, method, arguments);
    }

    @Override
    public void decompile(IndentPrintStream s) {}

    @Override
    protected void iterChildren(TreeFunction f) {this.getArguments().iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        this.getVariable().prettyPrint(s, prefix, true);
        this.getMethod().prettyPrint(s, prefix, true);
        this.getArguments().prettyPrint(s, prefix, true);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        //réserver emplacement paramètres +1
    	GPRegister r = compiler.getRegisterStart();
    	codeGenExpr(compiler, r);
    	compiler.freeRegister(r);
    }
    
    @Override
	public void codeGenExpr(DecacCompiler compiler, GPRegister op) {
    	int nombreParametres = getArguments().size();
    	compiler.addInstruction(new ADDSP(nombreParametres + 1));

    	compiler.addInstruction(new LOAD(((Identifier)getVariable()).getVariableDefinition().getOperand(), op));
    	compiler.addInstruction(new STORE(op, new RegisterOffset(0, GPRegister.SP)));
    	int spOffset =0;
    	for(AbstractExpr argument : getArguments().getList()) {
    		++spOffset;
    		argument.codeGenExpr(compiler, op);
    		compiler.addInstruction(new STORE(op, new RegisterOffset(- spOffset, GPRegister.SP)));
    		
    	}
    	compiler.addInstruction(new LOAD(new RegisterOffset(0, GPRegister.SP), op));
    	compiler.addInstruction(new CMP(new NullOperand(), op));
    	CompilerInstruction.codeGenErreur(compiler, new BEQ(CompilerInstruction.createErreurLabel(compiler, "deferencement.null", "Erreur : deferencement de null")));
    	compiler.addInstruction(new LOAD(new RegisterOffset(0, op), op));
    	System.out.println(getMethod().getName().getName() + " : "+ getMethod().getMethodDefinition().getIndex());
    	compiler.addInstruction(new BSR(new RegisterOffset(getMethod().getMethodDefinition().getIndex(), op)));
    	if(!getMethod().getDefinition().getType().isVoid()) {
    		compiler.addInstruction(new LOAD(GPRegister.R1, op));	
    	}
    	compiler.addInstruction(new SUBSP(nombreParametres + 1));
    }


    @Override
    public boolean isIntLiteral() {
        return false;
    }

    @Override
    public boolean isFloatLiteral() {
        return false;
    }

    @Override
    public boolean isBooleanLiteral() {
        return false;
    }

    @Override
    public boolean isIdentifier() {
        return false;
    }
    
    public boolean isMethod() {
    	return true;
    }

	@Override
	public boolean isThis() {
		return false;
	}

    
}

