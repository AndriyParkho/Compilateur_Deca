package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;

public class Dot extends AbstractLValue {
    private AbstractExpr objet;
    private AbstractIdentifier appel;

    public Dot(AbstractExpr gauche, AbstractIdentifier droite) {
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
    /**
     * on vérifie les points suivants:
     * l'objet a un type valide et qui correspond à une classe
     * l'appel est valide
     * l'attribut existe et correspond à un champs
     * la visibilité autorise l'appel
     */
    
    @Override
    public Type verifyExpr(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
        //FAIT
    	//vérification du type de l'objet 
    	Type typeObjet;
    	try {
    		typeObjet=objet.verifyExpr(compiler, currentClass);
    		if(!typeObjet.isClass())
    		{
    			throw new ContextualError("l'appel d'attribut est utilisable uniquement pour les instances de classe",this.getLocation());
    		}
    	}catch(ContextualError ce) {throw ce;}
    	
    	//vérification de l'appel dans l'environnement locale de la classe 
    	ClassType typeClass=(ClassType)typeObjet;
    	EnvironmentExp envGlobal=compiler.getEnvExp();
    	EnvironmentExp envLocal=typeClass.getDefinition().getMembers();
    	try {
    		compiler.setEnvExp(envLocal);
    		appel.verifyExpr(compiler, currentClass);
    		compiler.setEnvExp(envGlobal);
    	}catch(ContextualError ce) {throw ce;}
    	
    	//vérification de l'attribut (champs qui existe)
    	ExpDefinition attribut=envLocal.get(compiler.getSymbolTable().create(this.appel.getName().getName()));
    	if(attribut==null || !attribut.isField())
    	{
    		throw new ContextualError(String.format("l'attribut %s n'existe pas pour la classe %s", this.appel.getName().getName(),
                    this.objet.getType().getName().getName()),this.getLocation());
    	}
    	
    	//vérification de la visibilité
    	//à ce stade, l'attribut est bien un field
    	FieldDefinition defField=attribut.asFieldDefinition("", this.getLocation());
        if((defField.getVisibility()==Visibility.PROTECTED )&& (currentClass.getMembers().get(compiler.getSymbolTable().create(this.appel.getName().getName()))==null))
        {
        	throw new ContextualError(String.format("appel impossible de %s : champs protégé", defField.getType().getName().getName()),this.getLocation());
        }
        
        //si tout est bon, on fixe le type récupéré dans la définition
        Type type=defField.getType();
        this.setType(type);
        return type;
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
