package fr.ensimag.deca.tree;

import java.util.Iterator;

import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * List of expressions (eg list of parameters).
 *
 * @author gl10
 * @date 01/01/2021
 */
public class ListExpr extends TreeList<AbstractExpr> {


    @Override
    public void decompile(IndentPrintStream s) {
        Iterator<AbstractExpr> iterateur = this.iterator();
        while(iterateur.hasNext()) {
            iterateur.next().decompile(s);  
        }
    }
}
