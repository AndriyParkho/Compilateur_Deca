package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ParamDefinition;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 * Declaration of a parametre
 *
 * @author lagham
 * @date 14/01/2021
 */
public class DeclParam extends AbstractDeclParam {
	
	private AbstractIdentifier name;
	private AbstractIdentifier type;

	public AbstractIdentifier getType(){return type;}

	public DeclParam(AbstractIdentifier type , AbstractIdentifier name)
	{
		this.type=type;
		Validate.notNull(name);
		this.name=name;
	}
	
	/**
	 * on vérifie que le type est bon et on set le type et la définition
	 * @param compiler
	 * @param currentClass 
	 * @throws ContextualError
	 */
	@Override
	public  void verifyParamMembers(DecacCompiler compiler , EnvironmentExp lovalEnv , ClassDefinition currentClass)
            throws ContextualError{
		//FAIT
		Symbol symParam=compiler.getSymbolTable().create(this.type.getName().getName());
		TypeDefinition typeDefParam=compiler.getEnvTypes().get(symParam);
		//le type doit exister dans dans l'environnement des types 
		if(typeDefParam == null)
		{
			throw new ContextualError("type introuvable dans envTypes", this.getLocation());
		}
		//le type existe mais il faut qu'il soit différent de void
		else if(typeDefParam.getType().isVoid())
		{
			throw new ContextualError("un paramètre ne peut pas ètre de type void", this.getLocation());
		}
		else
		//le type est bon
		{
			this.type.setDefinition(typeDefParam);
			this.type.setType(typeDefParam.getType());
			ParamDefinition nameDef = new ParamDefinition(typeDefParam.getType(), this.getLocation());
			nameDef.setIndex(this.getIndex());
			this.name.setDefinition(nameDef);
			this.name.setType(typeDefParam.getType());
		}
	}
	/**
	 * vérification de l'initialisation et de l'unicité du nom
	 * @param compiler
	 * @param currentClass 
	 * @throws ContextualError
	 */
	@Override
    public void verifyParamBody(DecacCompiler compiler, EnvironmentExp lovalEnv , ClassDefinition currentClass)
            throws ContextualError{
		//FAIT
		Symbol paramSymbol= compiler.getSymbolTable().create(this.name.getName().getName());
		ParamDefinition defParam= new ParamDefinition(this.type.getType(),this.name.getLocation());
		//si le nom n'existe pas deja dans l'environnement, on ajoute e paramètre
		//sinon on lève une exception de double définition
		try {
			if(!lovalEnv.getDonnees().containsKey(paramSymbol))
			{
				lovalEnv.declare(paramSymbol, defParam);
			}
			else
			{
				throw new EnvironmentExp.DoubleDefException();
			}
		}catch(EnvironmentExp.DoubleDefException doubleDef) {
			throw new ContextualError("nom du paramètre déja utilisé",this.getLocation());
		}
	}

	@Override
    public void decompile(IndentPrintStream s) {
		this.type.decompile(s);
		s.print(" ");
		this.name.decompile(s);
    }
	
	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
			this.type.prettyPrint(s,prefix, false);
			this.name.prettyPrint(s, prefix, false);
	    }

	@Override
	protected void iterChildren(TreeFunction f) {
		type.iter(f);
		name.iter(f);
	}

	@Override
	public void codeGenParam(DecacCompiler compiler) {
		// A FAIRE : ou peut-être pas	
	}

	public void setParamOperand() {
		ParamDefinition paramDef = name.getParamDefinition();
		System.out.println("Dans le DECLPARAM : location du paramètre " + name.getName().getName() + " " + paramDef.getLocation());
		paramDef.setOperand(new RegisterOffset(-2 - getIndex(), Register.LB));
		System.out.println("Operand set : " + paramDef.getOperand());
	}
}
