package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;

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
        this.decompile(s);
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
    protected void codeGenExpr(DecacCompiler compiler, GPRegister op) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
        //FAIT
    	Type typeActuel;
    	Type typeCast;
    	try {
    		typeActuel=this.variable.verifyExpr(compiler, currentClass);
        	typeCast=this.type.verifyExpr(compiler, currentClass);
    	}catch(ContextualError ce) {throw ce;}
    	//il faut que le type actuel soit un sous type du type de cast 
    	//sinon le seul cas possible c'est la conversion int/float
    	if((!typeActuel.sousType(typeCast))&& !(typeActuel.isInt()&&typeCast.isFloat()))
    	{
    		throw new ContextualError("cast impossible",this.getLocation());
    	}
    	this.setType(typeCast);
    	return typeCast;
    	
    }

    @Override
    public boolean isBooleanLiteral() {
        // !
        return false;
    }
    
    @Override
    public boolean isFloatLiteral() {
        // !
        return false;
    }

    @Override
    public boolean isIdentifier() {
        // !
        return false;
    }

    @Override
    public boolean isIntLiteral() {
        // !
        return false;
    }

}
