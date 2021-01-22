package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CompilerInstruction;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
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
            ClassDefinition currentClass)
            throws ContextualError {
    	//Fait
    	Type typeVar;
    	typeVar=this.type.verifyType(compiler);
    	this.setLocation(this.type.getLocation());
    	if(typeVar.isVoid())
    	{
    		throw new ContextualError("une variable ne peut pas être de type void",this.getLocation());
    	}
    	//on peut alors définir cette variable, la déclarer et l'initialiser dans l'environnement local
    	try {
    		VariableDefinition defVar= new VariableDefinition(typeVar,this.varName.getLocation());
    		this.varName.setDefinition(defVar);
            this.initialization.verifyInitialization(compiler, typeVar, currentClass);
            if (this.initialization.getClass()==Initialization.class) {
                defVar.setType(((Initialization) this.initialization).getExpression().getType());
            }
            compiler.getEnvExp().declare(this.varName.getName(), defVar);
            compiler.getSymbolTable().create(this.varName.getName().getName());
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
        s.println(";");
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
    	// A FAIRE : Fonction non fini pour l'instant
    	DAddr varOperand;
    	System.out.println("On est dans une méthode : "+compiler.isInMethod());
    	if(!compiler.isInMethod()) {
	    	compiler.incrCountGB();
	    	varOperand = new RegisterOffset(compiler.getCountGB(), Register.GB);
    	}
    	else {
    		compiler.incrCountLB();
    		varOperand = new RegisterOffset(compiler.getCountLB(), Register.LB);
    	}
    	VariableDefinition varDef = varName.getVariableDefinition();
    	varDef.setOperand(varOperand);
    	if(initialization.isInitialization()) {
    		Initialization init = (Initialization) initialization;
    		init.getExpression().codeGenInst(compiler);
    	} 
    	else {
    		CompilerInstruction.initVarToZero(compiler, type, compiler.getRegisterStart());
    	}
    	compiler.addInstruction(new STORE(compiler.getRegisterStart(), varOperand));
    }
}
