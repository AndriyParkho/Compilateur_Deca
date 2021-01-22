package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.ParamDefinition;
import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.Dot;
import fr.ensimag.deca.tree.FloatLiteral;
import fr.ensimag.deca.tree.Identifier;
import fr.ensimag.deca.tree.IntLiteral;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

public class DValGetter {
	
	public static DVal getDVal(AbstractExpr e, DecacCompiler compiler) {
		// A Faire : C'est fait pour les opérations avec des entiers seulement
		if(e.isIntLiteral()) {
			IntLiteral intExpr = (IntLiteral) e;
			return new ImmediateInteger(intExpr.getValue());
		} else if(e.isIdentifier()) {
			//if(e.isMethod()) {
			//	return GPRegister.R1;
			//}
			Identifier identifierExpr = (Identifier) e;
			Definition identifierDef = identifierExpr.getDefinition();
			if(identifierDef.isParam()) {
				return ((ParamDefinition)identifierDef).getOperand();
			}
			else if(identifierDef.isField()) {
				compiler.addInstruction(new LOAD(new RegisterOffset(-2, GPRegister.LB), GPRegister.getR(2)));
				FieldDefinition newIdentifierDef = (FieldDefinition)identifierDef;
				newIdentifierDef.setOperand(new RegisterOffset(newIdentifierDef.getIndex(), GPRegister.getR(2)));
				return newIdentifierDef.getOperand();
			}

			return identifierExpr.getVariableDefinition().getOperand();
			
		} else if(e.isFloatLiteral()){
			FloatLiteral floatExpr = (FloatLiteral) e;
			return new ImmediateFloat(floatExpr.getValue());
		}else if(e.isDot()){
			Dot dot = (Dot) e;
			DVal objetDVal = DValGetter.getDVal(dot.getObjet(), compiler);
	    	compiler.addInstruction(new LOAD(objetDVal, Register.getR(3)));
	    	compiler.addInstruction(new CMP(new NullOperand(), Register.getR(3)));
	    	compiler.addInstruction(new BEQ(CompilerInstruction.createErreurLabel(compiler, "deferencement.null", "Erreur : deferencement de null")));
			return new RegisterOffset(dot.getAppel().getFieldDefinition().getIndex(), Register.getR(3));
		}
		else {
			return null;
		}
	}
}
