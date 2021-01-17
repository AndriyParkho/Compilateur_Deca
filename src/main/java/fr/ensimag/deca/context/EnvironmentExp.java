package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable.Symbol;
import org.apache.log4j.Logger;

import java.util.Hashtable;

/**
 * Dictionary associating identifier's ExpDefinition to their names.
 * 
 * This is actually a linked list of dictionaries: each EnvironmentExp has a
 * pointer to a parentEnvironment, corresponding to superblock (eg superclass).
 * 
 * The dictionary at the head of this list thus corresponds to the "current" 
 * block (eg class).
 * 
 * Searching a definition (through method get) is done in the "current" 
 * dictionary and in the parentEnvironment if it fails. 
 * 
 * Insertion (through method declare) is always done in the "current" dictionary.
 * 
 * @author gl10
 * @date 01/01/2021
 */
public class    EnvironmentExp {
    // A FAIRE : implémenter la structure de donnée représentant un
    // environnement (association nom -> définition, avec possibilité
    // d'empilement).

    EnvironmentExp parentEnvironment;
    Hashtable<Symbol,ExpDefinition> donnees = new Hashtable<Symbol,ExpDefinition>();
    
    public Hashtable<Symbol,ExpDefinition> getDonnees()
    {
    	return this.donnees;
    }
    
    public EnvironmentExp(EnvironmentExp parentEnvironment) {
        this.parentEnvironment = parentEnvironment;
    }

    public static class DoubleDefException extends RuntimeException {
        private static final long serialVersionUID = -2733379901827316441L;
    }

    /**
     * Return the definition of the symbol in the environment, or null if the
     * symbol is undefined.
     */
    public ExpDefinition get(Symbol key) {
        EnvironmentExp env_courant = this;
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
    public void declare(Symbol name, ExpDefinition def) throws DoubleDefException {
    	if (this.donnees.get(name) == null) {
    		this.donnees.put(name, def);
    	}
    	else {
    		throw new DoubleDefException();
    	}
    }


    public EnvironmentExp union(EnvironmentExp env) throws DoubleDefException{
        //Calcul de l'union de deux environnements. Renvoie une erreur contextuelle si indéfinie
        for (Symbol symbol_this : this.donnees.keySet()){
            for (Symbol symbol_env : env.donnees.keySet()){
                if (symbol_this.getName().equals(symbol_env.getName())){
                    throw new DoubleDefException();
                }
            }
        }
        EnvironmentExp Union;
        if (this.parentEnvironment== null && env.parentEnvironment == null) {
            Union = new EnvironmentExp(null);
        }
        else if (this.parentEnvironment == null){
            Union = new EnvironmentExp(env.parentEnvironment);
        }
        else if (env.parentEnvironment == null){
            Union = new EnvironmentExp(this.parentEnvironment);
        }
        else {
            Union = new EnvironmentExp(this.parentEnvironment.union(env.parentEnvironment));
        }
        Union.donnees.putAll(this.donnees);
        Union.donnees.putAll(env.donnees);
        return Union;
    }

}
