package fr.ensimag.deca.codegen;

import fr.ensimag.deca.*;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.ERROR;
import fr.ensimag.ima.pseudocode.instructions.TSTO;
import fr.ensimag.ima.pseudocode.instructions.WNL;
import fr.ensimag.ima.pseudocode.instructions.WSTR;
/*
 * Permet de factoriser les différentes instructions
 */
public class compilerInstruction {
	
	private static int decorationLigne = 50;
	
	public static void gestionPileVariablesGlobales(DecacCompiler compiler) {
		Label stack_overflow_error = labelErreurGeneration(compiler, "stack_overflow_error", "Erreur : débordement de pile dans la génération de variables globales");
		
		compiler.addInstructionBegin(new ADDSP(compiler.getCountVar()));
        compiler.addInstructionBegin(new BOV(stack_overflow_error));
        compiler.addInstructionBegin(new TSTO(compiler.getCountVar()));
	}
	
	public static Label labelErreurGeneration(DecacCompiler compiler, String nom, String error_message) {
        Label newLabelError = new Label(nom);
        
        decorationAssembleur(compiler, error_message);
        compiler.addLabel(newLabelError);
        compiler.addInstruction(new WSTR(error_message));
        compiler.addInstruction(new WNL());
        compiler.addInstruction(new ERROR());
        
        return newLabelError;
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
	
	public static Label labeSaut(DecacCompiler compiler, String nom) {
		Label labelSaut = new Label(nom);
		compiler.addLabel(labelSaut);
		return labelSaut;
	}
	
	
}
