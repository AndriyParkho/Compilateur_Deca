package fr.ensimag.deca.context;

import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tree.Location;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

/**
 * Definition of a class.
 *
 * @author gl10
 * @date 01/01/2021
 */
public class ClassDefinition extends TypeDefinition {

    private DAddr operand;
    private Set<MethodDefinition> methods = new TreeSet<MethodDefinition>(new MethodComparator());
    private Label initLabel;

    public void setNumberOfFields(int numberOfFields) {
        this.numberOfFields = numberOfFields;
    }

    public int getNumberOfFields() {
        return numberOfFields;
    }

    public void incNumberOfFields() {
        this.numberOfFields++;
    }

    public int getNumberOfMethods() {
        return numberOfMethods;
    }

    public void setNumberOfMethods(int n) {
        Validate.isTrue(n >= 0);
        numberOfMethods = n;
    }
    
    public int incNumberOfMethods() {
        numberOfMethods++;
        return numberOfMethods;
    }

    private int numberOfFields = 0;
    private int numberOfMethods = 0;
    
    @Override
    public boolean isClass() {
        return true;
    }
    
    @Override
    public ClassType getType() {
        // Cast succeeds by construction because the type has been correctly set
        // in the constructor.
        return (ClassType) super.getType();
    };

    public ClassDefinition getSuperClass() {
        return superClass;
    }

    private final EnvironmentExp members;
    private final ClassDefinition superClass; 

    public EnvironmentExp getMembers() {
        return members;
    }

    public ClassDefinition(ClassType type, Location location, ClassDefinition superClass) {
        super(type, location);
        EnvironmentExp parent;
        if (superClass != null) {
            parent = superClass.getMembers();
        } else {
            parent = null;
        }
        members = new EnvironmentExp(parent);
        this.superClass = superClass;
        // Création du label d'initialisation de la classe
        this.initLabel = new Label("init." + type.getName().getName());
    }
    
    public void setOperand(DAddr operand) {
        this.operand = operand;
    }

    public DAddr getOperand() {
        return operand;
    }

	public Set<MethodDefinition> getMethods() {
		return methods;
	}
    
    public void setMethodsTable() {
    	if(superClass != null) {
    		methods.addAll(superClass.getMethods());
    	}
    	for(ExpDefinition data : members.getDonnees().values()) {
    		if(data.isMethod()) {
    			MethodDefinition method = (MethodDefinition) data;
    			if(!methods.add(method)) {
    				methods.remove(method);
    				methods.add(method);
    			}
    			
    		}
    	}
    }
    
    public void codeGenMethodTable(DecacCompiler compiler) {
    	for(MethodDefinition method : methods) {
    		compiler.incrCountGB();
    		compiler.addInstruction(new LOAD(new LabelOperand(method.getLabel()), Register.R0));
    		compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(compiler.getCountGB(), Register.GB)));
    	}
    }

	public Label getInitLabel() {
		return initLabel;
	}

	public void setInitLabel(Label initLabel) {
		this.initLabel = initLabel;
	}
	
    
}
