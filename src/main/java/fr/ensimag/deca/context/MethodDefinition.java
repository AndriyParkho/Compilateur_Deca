package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tree.Location;
import fr.ensimag.ima.pseudocode.Label;
import org.apache.commons.lang.Validate;

/**
 * Definition of a method
 *
 * @author gl10
 * @date 01/01/2021
 */
public class MethodDefinition extends ExpDefinition {

	private int debutBloc;
	
    @Override
    public boolean isMethod() {
        return true;
    }

    public Label getLabel() {
        Validate.isTrue(label != null,
                "setLabel() should have been called before");
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public int getIndex() {
        return index;
    }
    public void setIndex(int index){ this.index=index; }

    private int index;

    @Override
    public MethodDefinition asMethodDefinition(String errorMessage, Location l)
            throws ContextualError {
        return this;
    }

    private final Signature signature;
    private Label label;

    private EnvironmentExp localEnv;

    public void setLocalEnv (EnvironmentExp EnvClass){
        localEnv = new EnvironmentExp(EnvClass);
    }

    public EnvironmentExp getLocalEnv() {
        return localEnv;
    }

    /**
     * 
     * @param type Return type of the method
     * @param location Location of the declaration of the method
     * @param signature List of arguments of the method
     * @param index Index of the method in the class. Starts from 0.
     */
    public MethodDefinition(Type type, Location location, Signature signature, int index) {
        super(type, location);
        this.signature = signature;
        this.index = index;
    }

    public Signature getSignature() {
        return signature;
    }

    @Override
    public String getNature() {
        return "method";
    }

    @Override
    public boolean isExpression() {
        return false;
    }

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof MethodDefinition) {
			MethodDefinition method = (MethodDefinition) obj;
			if(method.getIndex() == this.getIndex()) {
				return true;
			}
		}
		return false;
	}
	

	@Override
	public int hashCode() {
		return this.getIndex();
	}

	public int getDebutBloc() {
		return debutBloc;
	}

	public void setDebutBloc(int debutBloc) {
		this.debutBloc = debutBloc;
	}
    
    

}
