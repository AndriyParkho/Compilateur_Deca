package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.STORE;

/**
 * @author gl10
 * @date 01/01/2021
 */
public class DeclVar extends AbstractDeclVar {

    
    final private AbstractIdentifier type;
    final private AbstractIdentifier varName;
    final private AbstractInitialization initialization;

    public DeclVar(AbstractIdentifier type, AbstractIdentifier varName, AbstractInitialization initialization) {
        Validate.notNull(type);
        Validate.notNull(varName);
        Validate.notNull(initialization);
        this.type = type;
        this.varName = varName;
        this.initialization = initialization;
    }

    @Override
    protected void verifyDeclVar(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
    	//Fait
    	Type typeVar;
    	typeVar=this.type.verifyType(compiler);
    	if(typeVar.isVoid())
    	{
    		throw new ContextualError("une variable ne peut pas être de type void",this.getLocation());
    	}
    	//on peut alors définir cette variable, la déclarer et l'initialiser dans l'envoronnement local
    	try {
    		VariableDefinition defVar= new VariableDefinition(typeVar,this.varName.getLocation());
    		this.varName.setDefinition(defVar);
    		this.initialization.verifyInitialization(compiler, typeVar, localEnv, currentClass);
    		localEnv.declare(this.varName.getName(), defVar);
    	}catch(ContextualError ce)
    	{throw ce;}
    	 catch(EnvironmentExp.DoubleDefException doubleDefinition)
    	{throw new ContextualError("double définition d'une variable",this.getLocation());}
    	
    }

    
    @Override
    public void decompile(IndentPrintStream s) {
        type.decompile(s);
        s.print(" ");
        varName.decompile(s);
        initialization.decompile(s);
        s.print(";");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        type.iter(f);
        varName.iter(f);
        initialization.iter(f);
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }
    
    @Override
    protected void codeGenDeclVar(DecacCompiler compiler) {
    	compiler.incrCountVar();
    	DAddr varOperand = new RegisterOffset(compiler.getCountVar(), Register.GB);
    	VariableDefinition varDef = (VariableDefinition) varName.getDefinition();
    	varDef.setOperand(varOperand);
    	if(initialization.isInitialization()) {
    		Initialization init = (Initialization) initialization;
    		init.getExpression().codeGenInst(compiler);
    		compiler.addInstruction(new STORE(Register.getR(2), varOperand));
    	}
    }
}
