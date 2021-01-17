package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;

/**
 * Declaration of a field
 *
 * @author lagham
 * @date 14/01/2021
 */
public class DeclField extends AbstractDeclField {
	
	private AbstractIdentifier name;
	private AbstractIdentifier type;
	private AbstractInitialization initialization;
	private Visibility visib;
	
	public DeclField(AbstractIdentifier name , AbstractIdentifier type , AbstractInitialization initialization , Visibility visib)
	{
		this.name=name;
		this.type=type;
		this.initialization=initialization;
		this.visib=visib;
	}
	public AbstractIdentifier getName()
	{
		return this.name;
	}
	public AbstractIdentifier getType()
	{
		return this.type;
	}
	public Visibility getVisibility()
	{
		return this.visib;
	}
	
	/**
	 * vérification du type et du nom
	 * set definition et type
	 * @param compiler
	 * @param currentClass 
	 * @throws ContextualError
	 */
	@Override
	public  void verifyFieldMembers(DecacCompiler compiler  , ClassDefinition currentClass)
            throws ContextualError{
		//FAIT
		Symbol symField= compiler.getSymbolTable().create(type.getName().getName());
		TypeDefinition typeDefField= compiler.getEnvTypes().get(symField);
		//d'abord, on vérifie le type: défini, !void 
		if(typeDefField==null)
		{
			throw new ContextualError("type introuvable dans envType",this.getLocation());
		}
		else if(typeDefField.getType().isVoid())
		{
			throw new ContextualError("un champ ne peut pas etre de type void",this.getLocation());
		}
		//on peut alors faire la déclaration et les set
		currentClass.incNumberOfFields();
		FieldDefinition fieldDef= new FieldDefinition(typeDefField.getType(),this.getLocation(),
				this.getVisibility(),currentClass,currentClass.getNumberOfFields());
		try {
			if(!compiler.getEnvExp().getDonnees().containsKey(symField))
			{
				compiler.getEnvExp().declare(symField, fieldDef);
			}
			else
			{
				throw new EnvironmentExp.DoubleDefException();
			}
		}catch(EnvironmentExp.DoubleDefException doubleDef) {
			throw new ContextualError("nom de champ déja utilisé",this.getLocation());
		}
		this.name.setDefinition(typeDefField);
		this.name.setType(typeDefField.getType());
		this.type.setDefinition(fieldDef);
		this.type.setType(typeDefField.getType());
	}
	/**
	 * vérification de l'initialisation
	 */
	@Override
    public void verifyFieldBody(DecacCompiler compiler , ClassDefinition currentClass)
            throws ContextualError{
		//Fait
		try {
			Type t=this.type.verifyType(compiler);
			initialization.verifyInitialization(compiler, t, currentClass);
		}catch(ContextualError ce) {throw ce;}
	}
	

	@Override
    public void decompile(IndentPrintStream s) {
        if (visib.equals(Visibility.PROTECTED)) {
			s.print("protected");
		}
		type.decompile(s);
		s.print(" ");
		this.name.decompile(s);
		initialization.decompile(s);
		s.print(";");

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
