package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CompilerInstruction;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.INT;

public class CastExpr extends AbstractExpr{
    private AbstractIdentifier type;
    private AbstractExpr variable;

    public CastExpr(AbstractIdentifier type, AbstractExpr var) {
        this.type = type;
        this.variable = var;
    }
    
    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        this.type.decompile(s);
        s.print(") (");
        this.variable.decompile(s);
        s.print(")");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        this.type.prettyPrint(s, prefix, false);
        this.variable.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // TODO Auto-generated method stub
        
    }

    @Override
	public void codeGenExpr(DecacCompiler compiler, GPRegister op) {
    	Type typeType = type.getType();
    	Type varType = variable.getType();
        if(typeType.sameType(varType)) {
        	variable.codeGenExpr(compiler, op);
        } else if(typeType.isFloat() && varType.isInt()){
        	variable.codeGenExpr(compiler, op);
        	compiler.addInstruction(new FLOAT(op, op));
        } else if(typeType.isInt() && varType.isFloat()) {
        	variable.codeGenExpr(compiler, op);
        	compiler.addInstruction(new INT(op, op));
        } else if(typeType.isClass() && varType.isClassOrNull()) {
        	InstanceOf testCmpClass = new InstanceOf(type, variable);
        	testCmpClass.setLocation(this.getLocation());
        	String errMsg = "Erreur : Cast impossible Ligne " + getLocation().getLine() + " Position " + getLocation().getPositionInLine();
        	Label errLbl = CompilerInstruction.createErreurLabel(compiler, "cast_error_" + getLocation().getLine() + "_" + getLocation().getPositionInLine(),  errMsg);
        	Label castLbl = compiler.createLabel("cast_" + getLocation().getLine() + "_" + getLocation().getPositionInLine());
        	testCmpClass.codeGenSaut(compiler, true, castLbl, op);
        	CompilerInstruction.codeGenErreur(compiler, new BRA(errLbl));
        	compiler.addLabel(castLbl);
        	variable.codeGenExpr(compiler, op);
        }
        
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
        //FAIT
    	Type typeActuel;
    	Type typeCast;
    	try {
    		typeActuel=this.variable.verifyExpr(compiler, currentClass);
        	typeCast=this.type.verifyType(compiler);
    	}catch(ContextualError ce) {throw ce;}
    	//il faut que le type actuel soit un sous type du type de cast 
    	//sinon les seuls cas possibles c'est la conversion int/float et les opérations unités (boolean to boolean,
        //int to int et float to float)
    	if(!(typeCast.isClass() && typeActuel.isClass() && typeActuel.sousType(typeCast)) && !(typeCast.isInt()&&typeActuel.isFloat())
        && !(!typeActuel.isClass() && !typeCast.isClass() && typeActuel.sameType(typeCast)) && !(typeCast.isFloat() && typeActuel.isInt()))
    	{
    		throw new ContextualError(String.format("Un %s ne peut pas être cast dans un %s",
                    typeActuel.getName().getName(), typeCast.getName().getName()),this.getLocation());
    	}
    	this.setType(typeCast);
    	return typeCast;
    	
    }

}
