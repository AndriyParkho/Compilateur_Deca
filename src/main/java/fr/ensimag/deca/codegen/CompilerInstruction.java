package fr.ensimag.deca.codegen;

import fr.ensimag.deca.*;
import fr.ensimag.deca.tree.AbstractIdentifier;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.ERROR;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.TSTO;
import fr.ensimag.ima.pseudocode.instructions.WNL;
import fr.ensimag.ima.pseudocode.instructions.WSTR;
/*
 * Permet de factoriser les différentes instructions
 */
public class CompilerInstruction {
	
	private static int decorationLigne = 50;
	
	public static void gestionPileVariablesGlobales(DecacCompiler compiler) {		
		compiler.addInstructionBegin(new ADDSP(compiler.getCountGB()));
		if(!compiler.isVerificationTest())
			compiler.addInstructionBegin(new BOV(createErreurLabel(compiler, "stack_overflow_error", "Erreur : débordement de pile")));
		
        compiler.addInstructionBegin(new TSTO(compiler.getCountGB()+compiler.getTempMax()));
	}
	
	public static Label createErreurLabel(DecacCompiler compiler, String nom, String errorMessage) {
		Label newLabelError = compiler.createLabel(nom);
		if(!compiler.isVerificationTest()) {
			compiler.addErrLblList(newLabelError, errorMessage);
		}
		return newLabelError;
	}
	
	public static void labelErreurGeneration(DecacCompiler compiler, Label newLabelError, String errorMessage) {
        decorationAssembleur(compiler, errorMessage);
        compiler.addLabel(newLabelError);
        compiler.addInstruction(new WSTR(errorMessage));
        compiler.addInstruction(new WNL());
        compiler.addInstruction(new ERROR());
	}
	
	public static void codeGenErreur(DecacCompiler compiler, Instruction errInstr) {
		if(!compiler.isVerificationTest()) {
			compiler.addInstruction(errInstr);
		}
	}
	
	public static void decorationAssembleur(DecacCompiler compiler, String message) {
		int taille_message = message.length();
		String ligne= "";
		String demiEspace = "";
		for(int i = 0; i < decorationLigne; ++i) {
			ligne += "-";
		}
		int nombre_espace = (decorationLigne - taille_message)/2;
		for(int i = 0; i < nombre_espace; ++i) {
			demiEspace += " ";
		}
		compiler.addComment(ligne);
		compiler.addComment(demiEspace +message+ demiEspace);
		compiler.addComment(ligne);
	}
	
	public static void decorationLigne(DecacCompiler compiler, String message) {
		int tailleMessage = message.length();
		String ligne= "";
		int nombreTiret = (decorationLigne - tailleMessage)/2;
		for(int i = 0; i < nombreTiret - 1; ++i) {
			ligne += "-";
		}
		compiler.addComment(ligne +" "+ message);
	}
	
	public static Label labeSaut(DecacCompiler compiler, String nom) {
		Label labelSaut = new Label(nom);
		compiler.addLabel(labelSaut);
		return labelSaut;
	}
	
	public static void initVarToZero(DecacCompiler compiler, AbstractIdentifier type, GPRegister op) {
		if(type.getType().isInt()) {
			compiler.addInstruction(new LOAD(new ImmediateInteger(0), op));
		} else if(type.getType().isFloat()) {
			compiler.addInstruction(new LOAD(new ImmediateFloat(0.0f), op));
		} else if(type.getType().isBoolean()) {
			compiler.addInstruction(new LOAD(0, op));
		} else if(type.getType().isClass()) {
			compiler.addInstruction(new LOAD(new NullOperand(), op));
		} else {
			throw new UnsupportedOperationException("not supposed to use a "+type.getType().toString());
		}
	}
	
	public static String currentClassMethodString(DecacCompiler compiler) {
		return compiler.getCurrentClass().getName().getName().getName() + "." + compiler.getCurrentMethod().getName().getName().getName();
	}
	
	
}
