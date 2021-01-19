package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.Location;

/**
 * Type defined by a class.
 *
 * @author gl10
 * @date 01/01/2021
 */
public class ClassType extends Type {
    
    protected ClassDefinition definition;
    
    public ClassDefinition getDefinition() {
        return this.definition;
    }
            
    @Override
    public ClassType asClassType(String errorMessage, Location l) {
        return this;
    }

    @Override
    public boolean isClass() {
        return true;
    }

    @Override
    public boolean isClassOrNull() {
        return true;
    }

    /**
     * Standard creation of a type class.
     */
    public ClassType(Symbol className, Location location, ClassDefinition superClass) {
        super(className);
        this.definition = new ClassDefinition(this, location, superClass);
    }

    /**
     * Creates a type representing a class className.
     * (To be used by subclasses only)
     */
    protected ClassType(Symbol className) {
        super(className);
    }
    

    @Override
    public boolean sameType(Type otherType) {
        //Fait
        if (otherType.isClass()){
            return this.getDefinition()==((ClassType)otherType).getDefinition();
        }
    	return false;
    }

    /**
     * Return true if potentialSuperClass is a superclass of this class.
     */
    public boolean isSubClassOf(ClassType potentialSuperClass) {
        //Fait
    	if(potentialSuperClass.getName().getName().equals("Object"))
    	{
    		return true;
    	}
        if (this.getName().getName().equals("Object")){
            return false;
        }
    	else if(this.getDefinition().getType().sameType(potentialSuperClass.getDefinition().getType()))
    	{
    		return true;
    	}
    	else 
    	{
    	    System.out.println(this.getDefinition().getSuperClass().getType());
    		return this.getDefinition().getSuperClass().getType().isSubClassOf(potentialSuperClass);
    	}
    	
    }


}
