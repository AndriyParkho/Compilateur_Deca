package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

public class InitObjectClass {

	
	public static void initObjectMethodsTbl(DecacCompiler compiler) {
		ClassDefinition objectDefinition = (ClassDefinition) compiler.getEnvTypes().get(compiler.getSymbolTable().create("Object"));
		
		for(ExpDefinition data : objectDefinition.getMembers().getDonnees().values()) {
    		if(data.isMethod()) {
    			MethodDefinition method = (MethodDefinition) data;
    			method.setLabel(new Label("code." + objectDefinition.getType().getName().getName() + ".equals"));
    			objectDefinition.getMethods().add(method);
    		}
    	}
		
    	compiler.addComment("Construction de la table des méthodes de " + objectDefinition.getType().getName().getName());
    	compiler.incrCountGB();
    	compiler.addInstruction(new LOAD(new NullOperand(), Register.R0));
    	objectDefinition.setOperand(new RegisterOffset(compiler.getCountGB(), Register.GB));
		compiler.addInstruction(new STORE(Register.R0, objectDefinition.getOperand()));
		objectDefinition.codeGenMethodTable(compiler);
	}
}
