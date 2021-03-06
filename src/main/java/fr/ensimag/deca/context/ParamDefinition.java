package fr.ensimag.deca.context;

import fr.ensimag.deca.tree.Location;

/**
 * Definition of a method parameter.
 *
 * @author gl10
 * @date 01/01/2021
 */
public class ParamDefinition extends ExpDefinition {

    private int index;

    public int getIndex(){return index;}
    public void setIndex(int i){this.index = i;}

    public ParamDefinition(Type type, Location location) {
        super(type, location);
    }

    @Override
    public String getNature() {
        return "parameter";
    }

    @Override
    public boolean isExpression() {
        return true;
    }

    @Override
    public boolean isParam() {
        return true;
    }

}
