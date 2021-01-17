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

/**
 * Declaration of a parametre
 *
 * @author lagham
 * @date 14/01/2021
 */
public class DeclParam extends AbstractDeclParam {
	
	private AbstractIdentifier name;
	private AbstractIdentifier type;
	
	public DeclParam(AbstractIdentifier name , AbstractIdentifier type)
	{
		Validate.notNull(name);
		this.name=name;
		this.type=type;
	}
	
	/**
	 * on vérifie que le type est bon et on set le type et la définition
	 * @param compiler
	 * @param currentClass 
	 * @throws ContextualError
	 */
	@Override
	public  void verifyParamMembers(DecacCompiler compiler , ClassDefinition currentClass)
            throws ContextualError{
		//FAIT
		Symbol symParam=compiler.getSymbolTable().create(this.type.getName().getName());
		TypeDefinition typeDefParam=compiler.getEnvTypes().get(symParam);
		//le type doit exister dans dans l'environnement des types 
		if(typeDefParam == null)
		{
			throw new ContextualError("type introuvable dans envTypes", this.getLocation());
		}
		//le type existe mais il faut qu'il soit différent de void ou null
		else if(typeDefParam.getType().isNull()||typeDefParam.getType().isVoid())
		{
			throw new ContextualError("un paramètre ne peut pas ètre de type void ou null", this.getLocation());
		}
		else
		//le type est bon
		{
			this.type.setDefinition(typeDefParam);
			this.type.setType(typeDefParam.getType());
		}
		}
	/**
	 * vérification de l'initialisation et de l'unicité du nom
	 * @param compiler
	 * @param currentClass 
	 * @throws ContextualError
	 */
	@Override
    public void verifyParamBody(DecacCompiler compiler , ClassDefinition currentClass)
            throws ContextualError{
		//FAIT
		Symbol paramSymbol= compiler.getSymbolTable().create(this.name.getName().toString());
		ParamDefinition defParam= new ParamDefinition(this.type.getType(),this.name.getLocation());
		//si le nom n'existe pas deja dans l'environnement, on ajoute e paramètre
		//sinon on lève une exception de double définition
		try {
			if(!compiler.getEnvExp().getDonnees().containsKey(paramSymbol))
			{
				compiler.getEnvExp().declare(paramSymbol, defParam);
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
	        throw new UnsupportedOperationException("Not yet supported");
	    }

	@Override
	protected void iterChildren(TreeFunction f) {
	        throw new UnsupportedOperationException("Not yet supported");
	    }

}
