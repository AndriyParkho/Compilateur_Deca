package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable.Symbol;
import org.apache.log4j.Logger;
import java.util.Hashtable;

/**
 * Dictionary associating identifier's TypeDefinition to their names.
 * 
 * This is actually a linked list of dictionaries: each EnvironmentType has a
 * pointer to a parentEnvironment, corresponding to superblock (eg superclass).
 * 
 * The dictionary at the head of this list thus corresponds to the "current" 
 * block (eg class).
 * 
 * Searching a type (through method get) is done in the "current" 
 * dictionary and in the parentEnvironment if it fails. 
 * 
 * Insertion (through method declare) is always done in the "current" dictionary.
 * 
 * @author gl10
 * @date 01/01/2021
 */
public class EnvironmentType {
    // A FAIRE : implémenter la structure de donnée représentant un
    // environnement (association Type -> TypeDefinition, avec possibilité
    // d'empilement).

    EnvironmentType parentEnvironment;
    Hashtable<Symbol,TypeDefinition> donnees = new Hashtable<Symbol,TypeDefinition>();
    
    public EnvironmentType(EnvironmentType parentEnvironment) {
        this.parentEnvironment = parentEnvironment;
    }

    public static class DoubleDefException extends RuntimeException {
        private static final long serialVersionUID = -2733379901827316441L;
    }

    /**
     * Return the definition of the symbol in the environment, or null if the
     * symbol is undefined.
     */
    public TypeDefinition get(Symbol key) {
        EnvironmentType env_courant = this;
        while (env_courant != null){
            if (env_courant.donnees.get(key) != null){
                return env_courant.donnees.get(key);
            }
            else{
                env_courant = env_courant.parentEnvironment;
            }
        }
        return null;
    }

    /**
     * Add the definition def associated to the symbol name in the environment.
     * 
     * Adding a symbol which is already defined in the environment,
     * - throws DoubleDefException if the symbol is in the "current" dictionary 
     * - or, hides the previous declaration otherwise.
     * 
     * @param name
     *            Name of the symbol to define
     * @param def
     *            Definition of the symbol
     * @throws DoubleDefException
     *             if the symbol is already defined at the "current" dictionary
     *
     */
    public void declare(Symbol name, TypeDefinition def) throws DoubleDefException {
    	if (this.donnees.get(name) == null) {
    		this.donnees.put(name, def);
    	}
    	else {
    		throw new DoubleDefException();
    	}
    }
}
