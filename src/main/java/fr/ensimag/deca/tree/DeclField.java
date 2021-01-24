package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CompilerInstruction;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

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
	public  void verifyFieldMembers(DecacCompiler compiler  , ClassDefinition currentClass , int index)
            throws ContextualError{
		//FAIT
		Symbol symField= compiler.getSymbolTable().create(type.getName().getName());
		TypeDefinition typeDefField= compiler.getEnvTypes().get(symField);
		//d'abord, on vérifie le type: défini, !void 
		if(typeDefField==null)
		{
			throw new ContextualError("type inexistant",this.getLocation());
		}
		else if(typeDefField.getType().isVoid())
		{
			throw new ContextualError("un champs ne peut pas être de type void",this.getLocation());
		}
		//on peut alors faire la déclaration et les set
		FieldDefinition fieldDef;
		//on cherche d'abord l'index adéquat
		int vraiIndex=index;
		ExpDefinition envClassDef=currentClass.getMembers().get(this.name.getName());
		if(envClassDef!=null && envClassDef.isField())
		{
			FieldDefinition fieldClassDef= (FieldDefinition)envClassDef;
			vraiIndex=fieldClassDef.getIndex();
		}
		
		
		//currentClass.incNumberOfFields();
		fieldDef= new FieldDefinition(typeDefField.getType(),this.getLocation(),
				this.getVisibility(),currentClass,vraiIndex);
		try {
			currentClass.getMembers().declare(compiler.getSymbolTable().create(name.getName().getName()), fieldDef);
		}catch(EnvironmentExp.DoubleDefException doubleDef) {
			throw new ContextualError("nom de champ déja utilisé",this.getLocation());
		}
		compiler.getSymbolTable().create(name.getName().getName());
		this.name.setDefinition(fieldDef);
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
			s.print("protected ");
		}
		type.decompile(s);
		s.print(" ");
		this.name.decompile(s);
		initialization.decompile(s);
		s.println(";");

    }
	
	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
			type.prettyPrint(s, prefix, false);
			name.prettyPrint(s, prefix, false);
			initialization.prettyPrint(s, prefix, false);
	    }

	@Override
	protected void iterChildren(TreeFunction f) {
		name.iter(f);
		type.iter(f);
		initialization.iter(f);
	}
	
	@Override
	public void codeGenInitField(DecacCompiler compiler) {
		compiler.addComment("Initialisation de "+name.getName().getName());
		GPRegister r = compiler.getRegisterStart();
		if(initialization.isInitialization()) {
			Initialization init = (Initialization)initialization;
			init.getExpression().codeGenExpr(compiler, r);
			}
		else {
			CompilerInstruction.initVarToZero(compiler, type, r);
		}
		compiler.addInstruction(new LOAD(new RegisterOffset(-2, GPRegister.LB), GPRegister.R1));
		compiler.addInstruction(new STORE(r, new RegisterOffset(name.getFieldDefinition().getIndex(), GPRegister.R1)));
		compiler.freeRegister(r);
		
	}
	
	@Override
	public void codeGenInitExtendsField(DecacCompiler compiler, boolean toZero) {
		if(toZero) {
			compiler.addComment("Initialisation de "+name.getName().getName());
			CompilerInstruction.initVarToZero(compiler, type, Register.R0);
			compiler.addInstruction(new STORE(GPRegister.R0, new RegisterOffset(name.getFieldDefinition().getIndex(), GPRegister.R1)));		
		} else {
			if(initialization.isInitialization()) {
				compiler.addComment("Initialisation explicite de "+name.getName().getName());
				Initialization init = (Initialization)initialization;
				compiler.addInstruction(new LOAD(new RegisterOffset(-2, GPRegister.LB), GPRegister.R1));
				init.getExpression().codeGenExpr(compiler, GPRegister.R0);
				compiler.addInstruction(new STORE(GPRegister.R0, new RegisterOffset(name.getFieldDefinition().getIndex(), GPRegister.R1)));		
			}
		}
	}

}
