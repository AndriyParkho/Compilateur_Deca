package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.ERROR;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.WNL;
import fr.ensimag.ima.pseudocode.instructions.WSTR;

public class InitObjectClass {

	
	public static void initObjectMethodsTbl(DecacCompiler compiler) {
		ClassDefinition objectDefinition = (ClassDefinition) compiler.getEnvTypes().get(compiler.getSymbolTable().create("Object"));
		
		for(ExpDefinition data : objectDefinition.getMembers().getDonnees().values()) {
    		if(data.isMethod()) {
    			MethodDefinition method = (MethodDefinition) data;
    			method.setLabel(compiler.createLabel("code." + objectDefinition.getType().getName().getName() + ".equals"));
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
	
	public static void codeGenInitObject(DecacCompiler compiler) {
		CompilerInstruction.decorationAssembleur(compiler, "Initialisation des champs de Object");
		compiler.addLabel(compiler.createLabel("init.object"));
		compiler.addInstruction(new RTS());
		
	}
	
	public static void codeGenEqualsMethod(DecacCompiler compiler) {
		CompilerInstruction.decorationAssembleur(compiler, "Classe Object");
		CompilerInstruction.decorationLigne(compiler, "equals");
		compiler.addLabel(compiler.createLabel("code.Object.equals"));
		compiler.addInstruction(new PUSH(GPRegister.getR(2)));
		compiler.addInstruction(new PUSH(GPRegister.getR(3)));
		compiler.addInstruction(new LOAD(new RegisterOffset(-2, GPRegister.LB), GPRegister.getR(2))); //adresse de l'objet dans R2
		compiler.addInstruction(new LOAD(new RegisterOffset(-3, GPRegister.LB), GPRegister.getR(3)));
		compiler.addInstruction(new CMP(GPRegister.getR(2), GPRegister.getR(3)));
		compiler.addInstruction(new BNE(compiler.createLabel("object.notEqual")));
		//set RO a true
		compiler.addInstruction(new LOAD(1, GPRegister.R1));
		//BRA fin
		compiler.addInstruction(new BRA(compiler.createLabel("fin.Object.equals")));
		//partie erreur sortie sans return
		compiler.addInstruction(new WSTR("Erreur : sortie de la méthode Object.equals sans return"));
		compiler.addInstruction(new WNL());
		compiler.addInstruction(new ERROR());
		//label faux
		compiler.addLabel(compiler.createLabel("object.notEqual"));
		//set R0 a false
		compiler.addInstruction(new LOAD(0, GPRegister.R1));
		//BRA fin
		compiler.addInstruction(new BRA(compiler.createLabel("fin.Object.equals")));
		//partie erreur sortie sans return
		compiler.addInstruction(new WSTR("Erreur : sortie de la méthode Object.equals sans return"));
		compiler.addInstruction(new WNL());
		compiler.addInstruction(new ERROR());
		//label fin
		compiler.addLabel(compiler.createLabel("fin.Object.equals"));
		//RTS

		compiler.addInstruction(new POP(GPRegister.getR(2)));
		compiler.addInstruction(new POP(GPRegister.getR(3)));
		compiler.addInstruction(new RTS());
	}
}
