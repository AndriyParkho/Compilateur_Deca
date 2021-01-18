package fr.ensimag.deca.tree;

import fr.ensimag.deca.CompilerOptions;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.SymbolTable;

import java.io.File;

/**
 * Test de la structure de methodCall et de verifyExpr. A modifier pour tester tous les cas d'erreurs contextuelles
 *
 * @author gl10
 * @date 18/01/2021
 */
public class MethodCallStructuralTest {
    public static void main(String[] args) throws Exception{
        CompilerOptions option = new CompilerOptions();
        File fichier = new File("test.txt");
        DecacCompiler compiler = new DecacCompiler(option, fichier);
        Location loc = new Location(1,2, "test.txt");

        SymbolTable.Symbol classeASymbol=compiler.getSymbolTable().create("A");
        ClassDefinition classADef = new ClassDefinition(new ClassType(classeASymbol, loc, ((ClassDefinition) compiler.getEnvTypes().get(compiler.getSymbolTable().create("Object")))),
                loc, ((ClassDefinition) compiler.getEnvTypes().get(compiler.getSymbolTable().create("Object"))));
        compiler.getEnvTypes().declare(classeASymbol, (ClassDefinition) classADef);

        SymbolTable.Symbol classeBSymbol=compiler.getSymbolTable().create("B");
        ClassDefinition classBDef = new ClassDefinition(new ClassType(classeASymbol, loc, ((ClassDefinition) compiler.getEnvTypes().get(compiler.getSymbolTable().create("Object")))),
                loc, ((ClassDefinition) compiler.getEnvTypes().get(compiler.getSymbolTable().create("Object"))));
        compiler.getEnvTypes().declare(classeBSymbol, (ClassDefinition) classBDef);

        Signature sig = new Signature();
        sig.add(classADef.getType());
        MethodDefinition methode_test = new MethodDefinition(compiler.getEnvTypes().get(compiler.getSymbolTable().create("void")).getType(),
                loc, sig, 1);
        SymbolTable.Symbol method_testSymbol = compiler.getSymbolTable().create("method_test");
        ((ClassDefinition) compiler.getEnvTypes().get(classeASymbol)).getMembers().declare(method_testSymbol, methode_test);

        SymbolTable.Symbol x = compiler.getSymbolTable().create("x");
        compiler.getEnvExp().declare(x, new VariableDefinition(((ClassDefinition) classADef).getType() ,loc));

        Identifier X = new Identifier(x);
        Identifier Method_test = new Identifier(method_testSymbol);
        Method_test.setDefinition(((ClassDefinition) compiler.getEnvTypes().get(classeASymbol)).getMembers().get(method_testSymbol));
        Method_test.setType(methode_test.getType());

        ListExpr arguments = new ListExpr();
        arguments.add(X);

        MethodCall method = new MethodCall(X, Method_test, arguments);
        method.verifyExpr(compiler, null);
    }
}
