package fr.ensimag.deca;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * User-specified options influencing the compilation.
 *
 * @author gl10
 * @date 01/01/2021
 */
public class CompilerOptions {
    public static final int QUIET = 0;
    public static final int INFO  = 1;
    public static final int DEBUG = 2;
    public static final int TRACE = 3;
    public int getDebug() {
        return debug;
    }

    public boolean getParallel() {
        return parallel;
    }

    public boolean getPrintBanner() {
        return printBanner;
    }
    
    
    public List<File> getSourceFiles() {
        return Collections.unmodifiableList(sourceFiles);
    }

    private int debug = 0;
    private boolean parallel = false;
    private boolean printBanner = false;
    private boolean verification = false;
    private boolean parse = false;
    private boolean suppressionTest = false;
    private int nombreRegistreMax = 16;
    private List<File> sourceFiles = new ArrayList<File>();

    
    public void parseArgs(String[] args) throws CLIException {
    	
    	if(args.length != 0 && args[0].equals("-b")) {
    		printBanner = true;
			if(args.length > 1) {
				throw new CLIException("L'option -b ne peut s'utiliser que seule");
			}
    	}else {
    		
    		boolean isRegister = false;
    		
	    	for(String param : args) { //pour chaque paramètre
	    		if(isRegister) {
	    			try {
	    				nombreRegistreMax = Integer.parseInt(param);
	    			}catch(NumberFormatException e){
	    				throw new CLIException("L'option -r doit être suivi d'un entier valide compris entre 4<=X<=16");
	    			}
	    			isRegister = false;
	    			continue;
	    		}
	    		switch(param){    		
	    		case "-P" : parallel = true;
	    					break;
	    					
	    		case "-d" : debug ++;
	    					break;
	    					
	    		case "-v" : verification = true;
	    					break;
	    					
	    		case "-p" : parse = true;
	    					break;
	    		
	    		case "-r" : isRegister = true;
	    					break; 
	    					
	    		case "-n" : suppressionTest = true;
	    					break;
	    				
	    		default : sourceFiles.add(new File(param));
	    				  break;
	    		}
	    	
	    	}
	    	
	    	if(isVerification() && isParse()) {
	    		throw new CLIException("Les options -p et -v ne peuvent être utilisées que séparément");

	    	}
	    	
	    	if(nombreRegistreMax > 16 || nombreRegistreMax < 4) {
	    		throw new CLIException("Les nomnbre de registres disponibles doit être compris entre 4 et 16");
	    	}
    	}
    	
    	
        Logger logger = Logger.getRootLogger();
        // map command-line debug option to log4j's level.
        switch (getDebug()) {
        
        case QUIET: break; // keep default
        case INFO:
            logger.setLevel(Level.INFO); break;
        case DEBUG:
            logger.setLevel(Level.DEBUG); break;
        case TRACE:
            logger.setLevel(Level.TRACE); break;
        default:
            logger.setLevel(Level.ALL); break;
        }
        logger.info("Application-wide trace level set to " + logger.getLevel());

        boolean assertsEnabled = false;
        assert assertsEnabled = true; // Intentional side effect!!!
        if (assertsEnabled) {
            logger.info("Java assertions enabled");
        } else {
            logger.info("Java assertions disabled");
        }
        
        
    }

    public int getNombreRegistreMax() {
		return nombreRegistreMax;
	}

	public boolean isVerification() {
		return verification;
	}

	public boolean isParse() {
		return parse;
	}
	
	public boolean isSuppressionTest() {
		return suppressionTest;
	}

	protected void displayUsage() {
        System.out.println("decac [[-p | -v] [-n] [-r X] [-d]* [-P] [-W] <fichier deca>...] | [-b]");
        System.out.println("La commande decac sans argument affiche les options disponibles.");
    }
}
