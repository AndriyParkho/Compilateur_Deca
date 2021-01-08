package fr.ensimag.deca.context;
import fr.ensimag.deca.tools.*;
import fr.ensimag.deca.tree.*;

import org.apache.log4j.Logger;



public class TestCreationEnvironnementBasic1 {
	
	private static final Logger LOG = Logger.getLogger(TestCreationEnvironnementBasic1.class);
	
	public static void main(String[] args){
		EnvironmentExp environment = new EnvironmentExp(null);
		SymbolTable table = new SymbolTable();
		
		Location location = new Location(1,1,"test.txt");
		
		IntType Int = new IntType(table.create("int"));
		VariableDefinition x = new VariableDefinition(Int, location);
		BooleanType Bool = new BooleanType(table.create("bool"));
		VariableDefinition a = new VariableDefinition(Bool, location);

		
		try {
			environment.declare(Int.getName(), x);
			environment.declare(Bool.getName(), a);
			environment.declare(Bool.getName(), a);
		}
		catch(Exception e){	
			LOG.info("erreur");
		}
	}
}
