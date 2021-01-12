package fr.ensimag.deca.codegen;

import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.Identifier;
import fr.ensimag.deca.tree.IntLiteral;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.ImmediateInteger;

public class DValGetter {
	
	public static DVal getDVal(AbstractExpr e) {
		// A Faire : C'est fait pour les opérations avec des entiers seulement
		if(e.isIntLiteral()) {
			IntLiteral intExpr = (IntLiteral) e;
			return new ImmediateInteger(intExpr.getValue());
		} else if(e.isIdentifier()) {
			Identifier identifierExpr = (Identifier) e;
			return identifierExpr.getVariableDefinition().getOperand();
		} else {
			return null;
		}
	}
}
