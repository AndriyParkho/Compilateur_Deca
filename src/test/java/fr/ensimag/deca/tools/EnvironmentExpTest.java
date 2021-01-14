package fr.ensimag.deca.tools;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tree.Location;

public class EnvironmentExpTest {
    public static void main(String[] args) throws Exception{
        SymbolTable symbolTable = new SymbolTable();
        Location loc = new Location(1, 1, "test.txt");
        EnvironmentExp env1 = new EnvironmentExp(null);

        SymbolTable.Symbol xSymbol = symbolTable.create("x");
        SymbolTable.Symbol ySymbol = symbolTable.create("y");
        SymbolTable.Symbol zSymbol = symbolTable.create("z");
        SymbolTable.Symbol tSymbol = symbolTable.create("t");
        VariableDefinition xDef = new VariableDefinition(new IntType(xSymbol), loc);
        VariableDefinition yDef = new VariableDefinition(new IntType(ySymbol), loc);
        VariableDefinition zDef = new VariableDefinition(new IntType(zSymbol), loc);
        VariableDefinition tDef = new VariableDefinition(new IntType(tSymbol), loc);
        try {
            env1.declare(xSymbol, xDef);
            env1.declare(ySymbol, yDef);
            env1.declare(zSymbol, zDef);
            env1.declare(tSymbol, tDef);
        } catch(EnvironmentExp.DoubleDefException e){throw e;}

        EnvironmentExp env2 = new EnvironmentExp(env1);
        SymbolTable.Symbol aSymbol = symbolTable.create("a");
        VariableDefinition aDef = new VariableDefinition(new IntType(aSymbol), loc);
        env2.declare(aSymbol, aDef);

        System.out.println("\nenv2");
        System.out.println(env2.get(aSymbol));
        System.out.println(env2.get(xSymbol));

        env2.declare(xSymbol, yDef);
        System.out.println(env2.get(xSymbol));

        EnvironmentExp env3 = new EnvironmentExp(null);
        env3.declare(aSymbol, aDef);

        EnvironmentExp env31 = env1.union(env3);
        System.out.println("\nenv31");
        System.out.println(env31.get(xSymbol));
        System.out.println(env31.get(ySymbol));
        System.out.println(env31.get(zSymbol));
        System.out.println(env31.get(tSymbol));
        System.out.println(env31.get(aSymbol));

        EnvironmentExp env4 = new EnvironmentExp(null);
        SymbolTable.Symbol bSymbol = symbolTable.create("b");
        VariableDefinition bDef = new VariableDefinition(new IntType(bSymbol), loc);
        env4.declare(bSymbol, bDef);

        EnvironmentExp env42 = env2.union(env4);
        System.out.println("\nenv42");
        System.out.println(env42.get(xSymbol));
        System.out.println(env42.get(zSymbol));
        System.out.println(env42.get(ySymbol));
        System.out.println(env42.get(tSymbol));
        System.out.println(env42.get(aSymbol));
        System.out.println(env42.get(bSymbol));

        EnvironmentExp env5 = new EnvironmentExp(null);
        SymbolTable.Symbol cSymbol = symbolTable.create("c");
        VariableDefinition cDef = new VariableDefinition(new IntType(cSymbol), loc);
        env5.declare(cSymbol, cDef);

        EnvironmentExp env6 = new EnvironmentExp(env5);
        SymbolTable.Symbol dSymbol = symbolTable.create("d");
        VariableDefinition dDef = new VariableDefinition(new IntType(dSymbol), loc);
        env6.declare(dSymbol, dDef);

        EnvironmentExp env62 = env2.union(env6);
        System.out.println("\nenv62");
        System.out.println(env62.get(xSymbol));
        System.out.println(env62.get(zSymbol));
        System.out.println(env62.get(ySymbol));
        System.out.println(env62.get(tSymbol));
        System.out.println(env62.get(aSymbol));
        System.out.println(env62.get(cSymbol));
        System.out.println(env62.get(dSymbol));
    }
}