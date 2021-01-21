package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.ParamDefinition;
import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.FloatLiteral;
import fr.ensimag.deca.tree.Identifier;
import fr.ensimag.deca.tree.IntLiteral;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

public class DValGetter {
	
	public static DVal getDVal(AbstractExpr e, DecacCompiler compiler) {
		// A Faire : C'est fait pour les opérations avec des entiers seulement
		if(e.isIntLiteral()) {
			IntLiteral intExpr = (IntLiteral) e;
			return new ImmediateInteger(intExpr.getValue());
		} else if(e.isIdentifier()) {
			Identifier identifierExpr = (Identifier) e;
			Definition identifierDef = identifierExpr.getDefinition();
			if(identifierDef.isParam()) {
				return ((ParamDefinition)identifierDef).getOperand();
			}
			else if(identifierDef.isField()) {
				compiler.addInstruction(new LOAD(new RegisterOffset(-2, GPRegister.LB), compiler.getRegisterStart()));
				return ((ExpDefinition)identifierDef).getOperand();
			}
			return identifierExpr.getVariableDefinition().getOperand();
		} else if(e.isFloatLiteral()){
			FloatLiteral floatExpr = (FloatLiteral) e;
			return new ImmediateFloat(floatExpr.getValue());
		} else {
			return null;
		}
	}
}
