package fr.ensimag.deca.context;

import java.util.Comparator;

/*
 * Fonction qui permet de comparer deux méthodes pour le tableau des méthodes d'une classe 
 */
public class MethodComparator implements Comparator<MethodDefinition> {

	@Override
	public int compare(MethodDefinition m1, MethodDefinition m2) {
		if(m1.getIndex() > m2.getIndex()) return 1;
		else return 0;
	}

}
