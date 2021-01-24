package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CompilerInstruction;
import fr.ensimag.deca.codegen.DValGetter;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

public class Dot extends AbstractLValue {
    private AbstractExpr objet;
    private AbstractIdentifier appel;

    public Dot(AbstractExpr gauche, AbstractIdentifier droite) {
        this.objet = gauche;
        this.appel = droite;
    }

    @Override
	public void codeGenExpr(DecacCompiler compiler, GPRegister op) {
        DVal valueDVal = DValGetter.getDVal(objet, compiler);
        if(valueDVal == null) {
        	objet.codeGenExpr(compiler, op);
        } else {
        	compiler.addInstruction(new LOAD(valueDVal, op));
        }
        compiler.addInstruction(new CMP(new NullOperand(), op));
        CompilerInstruction.codeGenErreur(compiler, new BEQ(CompilerInstruction.createErreurLabel(compiler, "deferencement.null", "Erreur : deferencement de null")));
    	compiler.addInstruction(new LOAD(new RegisterOffset(getAppel().getFieldDefinition().getIndex(), op), op));
    }
    
    @Override
	protected void codeGenSaut(DecacCompiler compiler, boolean eval, Label etiquette, GPRegister op) {
    	this.codeGenExpr(compiler, op);
        int saut = eval ? 1 : 0;
    	compiler.addInstruction(new CMP(saut ,op));
    	compiler.addInstruction(new BEQ(etiquette));
	}

	@Override
    public DVal codeGenAssignDot(DecacCompiler compiler, GPRegister op) {
    	DVal valueDVal = DValGetter.getDVal(objet, compiler);
        if(valueDVal == null) {
        	objet.codeGenAssignDot(compiler, op);
            compiler.addInstruction(new CMP(new NullOperand(), op));
            CompilerInstruction.codeGenErreur(compiler, new BEQ(CompilerInstruction.createErreurLabel(compiler, "deferencement.null", "Erreur : deferencement de null")));
        	compiler.addInstruction(new LOAD(new RegisterOffset(getAppel().getFieldDefinition().getIndex(), op), op));
        } else {
        	compiler.addInstruction(new LOAD(valueDVal, op));
        	compiler.addInstruction(new CMP(new NullOperand(), op));
        	CompilerInstruction.codeGenErreur(compiler, new BEQ(CompilerInstruction.createErreurLabel(compiler, "deferencement.null", "Erreur : deferencement de null")));
        }
        return new RegisterOffset(getAppel().getFieldDefinition().getIndex(), op);
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
        if (defField.getVisibility()==Visibility.PROTECTED && currentClass == null){
            throw new ContextualError(String.format("appel impossible de %s : champs protégé", this.appel.getName().getName()),this.getLocation());
        }
        else if(defField.getVisibility()==Visibility.PROTECTED && (!((ClassType)this.objet.getType()).isSubClassOf(currentClass.getType())
                || !(currentClass.getType().isSubClassOf(this.appel.getFieldDefinition().getContainingClass().getType()))))
        {
        	throw new ContextualError(String.format("appel impossible de %s : champs protégé", this.appel.getName().getName()),this.getLocation());
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
    
    

    public AbstractExpr getObjet() {
		return objet;
	}

	public AbstractIdentifier getAppel() {
		return appel;
	}
	
    @Override
	public boolean isDot() {
		return true;
	}
}
