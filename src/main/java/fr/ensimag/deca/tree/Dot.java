package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;

public class Dot extends AbstractLValue {
    private AbstractExpr objet;
    private AbstractExpr appel;

    public Dot(AbstractExpr gauche, AbstractExpr droite) {
        this.objet = gauche;
        this.appel = droite;
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler, boolean printHex) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler, GPRegister op) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void decompile(IndentPrintStream s) {
        this.objet.decompile(s);
        s.print(".");
        this.appel.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        this.objet.prettyPrint(s, prefix, false);
        this.appel.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // TODO Auto-generated method stub
        
    }

    @Override
	public boolean isIntLiteral() {
		// !
		return false;
	}

	@Override
	public boolean isFloatLiteral() {
		// !
		return false;
	}

	@Override
	public boolean isBooleanLiteral() {
		// !
		return false;
	}

	@Override
	public boolean isIdentifier() {
		// !
		return false;
	}
}
