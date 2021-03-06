package fr.ensimag.deca;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.log4j.Logger;

import fr.ensimag.deca.codegen.CompilerInstruction;
import fr.ensimag.deca.context.BooleanType;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.EnvironmentType;
import fr.ensimag.deca.context.FloatType;
import fr.ensimag.deca.context.IntType;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.context.VoidType;
import fr.ensimag.deca.syntax.DecaLexer;
import fr.ensimag.deca.syntax.DecaParser;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.AbstractProgram;
import fr.ensimag.deca.tree.DeclClass;
import fr.ensimag.deca.tree.DeclMethod;
import fr.ensimag.deca.tree.Location;
import fr.ensimag.deca.tree.LocationException;
import fr.ensimag.deca.tree.RegisterException;
import fr.ensimag.ima.pseudocode.AbstractLine;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.IMAProgram;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;

/**
 * Decac compiler instance.
 *
 * This class is to be instantiated once per source file to be compiled. It
 * contains the meta-data used for compiling (source file name, compilation
 * options) and the necessary utilities for compilation (symbol tables, abstract
 * representation of target file, ...).
 *
 * It contains several objects specialized for different tasks. Delegate methods
 * are used to simplify the code of the caller (e.g. call
 * compiler.addInstruction() instead of compiler.getProgram().addInstruction()).
 *
 * @author gl10
 * @date 01/01/2021
 */
public class DecacCompiler {
    private static final Logger LOG = Logger.getLogger(DecacCompiler.class);
    
    /*
     * Compteur de variable global utilisé
     */
    
    
    private int countGB = 0;
    private int countLB = 0;
    
    private int tempPile = 0;
    private int maxTempPile = 0;
    
    private int tempPileMethod = 0;
    private int maxTempPileMethod = 0;
    
    private boolean inMethod = false;
    
    private DeclClass currentClass;
    private DeclMethod currentMethod;
    
    private boolean[] freeRegister;
    
    public DeclClass getCurrentClass() {
    	return currentClass;
    }
    
    public void setCurrentClass(DeclClass newClass) {
    	currentClass = newClass;
    }
    
    public DeclMethod getCurrentMethod() {
    	return currentMethod;
    }
    
    public void setCurrentMethod(DeclMethod newMethod) {
    	currentMethod = newMethod;
    }
    
    public boolean isInMethod() {
    	return inMethod;
    }
    
    public void setIsInMethod(boolean newValeur) {
    	inMethod = newValeur;
    }
    
    public void incementTempPileMethod() {
    	tempPileMethod ++;
    	if(tempPileMethod > maxTempPileMethod) {
    		maxTempPileMethod = tempPileMethod;
    	}
    }
    
    public void decrementTempPileMethod() {
    	tempPileMethod --;
    }
    
    public void setTempPileMethod(int nouveau) {
    	tempPileMethod = nouveau;
    }
    
    public int getMaxTempPileMethod() {
    	return maxTempPileMethod;
    }
    
    public void incrementTempPile() {
    	if(!isInMethod()) {
	    	tempPile ++;
	    	if(tempPile > maxTempPile) {
	    		maxTempPile = tempPile;
	    	}	    	
    	}else {
    		tempPileMethod++;
    		if(tempPileMethod > maxTempPileMethod) {
    			maxTempPileMethod = tempPileMethod;
    		}
    	}
    }
    
    public void decrementTempPile() {
    	tempPile --;
    }
    
    public int getTempMax() {
    	return maxTempPile;
    }
    
    public void setTempPile(int nouvelleValeur) {
    	tempPile = nouvelleValeur;
    }
    
    public void setMaxTempPile(int nouvelleValeur) {
    	maxTempPile = nouvelleValeur;
    }
    
    public int getCountLB() {
    	return countLB;
    }
    
    public void incrCountLB() {
    	countLB++;
    }
    
    public void setCountLB(int newValeur) {
    	countLB = newValeur;
    }
    private Map<Label, String> errLblList = new HashMap<Label, String>();
    private Map<String, Label> lblMap = new HashMap<String, Label>();
    
    /**
     * Portable newline character.
     */
    private static final String nl = System.getProperty("line.separator", "\n");
  
    //med,ali:j'ai ajouté cette instance car j'en ai besoin pour la vérification contextuelle  
   private SymbolTable symbolTable;
   public SymbolTable getSymbolTable()
   {
	   return symbolTable;
   }
   //envTypes contient les types predefinis
   //il faut l'initialiser avec les types prédéfinis
   private EnvironmentType envTypes;
    //envExp contient les exp predefinis
    //il faut l'initialiser avec les exp prédéfinis
    private EnvironmentExp envExpPre;

    public EnvironmentType getEnvTypes()
   {
	   return this.envTypes;
   }
    public EnvironmentExp getEnvExpPre()
    {
        return this.envExpPre;
    }

    private EnvironmentExp envExp;
    public EnvironmentExp getEnvExp()
    {
        return this.envExp;
    }
    public void setEnvExp(EnvironmentExp env){ this.envExp = env; }
   /**
    * méthode qui initialise l'environnement des types en y insérant 
    * tous les types prédéfinis
    */
   public void envTypesInit () /*throws EnvironmentType.DoubleDefException**/
   {
	   Symbol intSymbol=this.symbolTable.create("int");
	   Symbol floatSymbol=this.symbolTable.create("float");
	   Symbol boolSymbol=this.symbolTable.create("boolean");
	   Symbol voidSymbol=this.symbolTable.create("void");
	   Symbol objectSymbol=this.symbolTable.create("Object");

	   TypeDefinition intTypeDef = new TypeDefinition(new IntType(intSymbol),Location.BUILTIN);
	   TypeDefinition floatTypeDef = new TypeDefinition(new FloatType(floatSymbol),Location.BUILTIN );
	   TypeDefinition boolTypeDef = new TypeDefinition(new BooleanType(boolSymbol),Location.BUILTIN);
	   TypeDefinition voidTypeDef = new TypeDefinition(new VoidType(voidSymbol),Location.BUILTIN);
	   ClassDefinition objectClassDef = new ClassDefinition(new ClassType(objectSymbol, Location.BUILTIN, null),
                                                            Location.BUILTIN, null);

       this.envTypes.declare(floatSymbol, floatTypeDef);
       this.envTypes.declare(boolSymbol, boolTypeDef);
       this.envTypes.declare(voidSymbol, voidTypeDef);
       this.envTypes.declare(intSymbol, intTypeDef);
       this.envTypes.declare(objectSymbol, objectClassDef);
   }

   public void envExpInit() {
       this.envExpPre = new EnvironmentExp(null);
       Symbol equalsSymbol=this.symbolTable.create("equals");
       Signature sig = new Signature();
       sig.add(this.envTypes.get(this.symbolTable.create("Object")).getType());
       MethodDefinition equalsDef = new MethodDefinition(this.envTypes.get(this.symbolTable.create("boolean")).getType(),
                                                         Location.BUILTIN, sig, 1);
       ((ClassDefinition)this.getEnvTypes().get(this.getSymbolTable().create("Object"))).getMembers().declare(equalsSymbol, equalsDef);
       this.envExpPre.declare(equalsSymbol, equalsDef);
       envExp = envExpPre;
   }
   
   
    public DecacCompiler(CompilerOptions compilerOptions, File source) {
        super();
        this.compilerOptions = compilerOptions;
        this.source = source;
        this.symbolTable = new SymbolTable();
        this.envTypes= new EnvironmentType(null);
        this.envTypesInit();
        this.envExpInit();
        this.nombreRegistres = compilerOptions.getNombreRegistreMax();
        this.verificationTest = compilerOptions.isSuppressionTest();
        this.freeRegister = new boolean[nombreRegistres - 2];
        for(int i = 0; i < nombreRegistres - 2 ; i++)
        	freeRegister[i] = true;
    }

    /**
     * Source file associated with this compiler instance.
     */
    public File getSource() {
        return source;
    }

    /**
     * Compilation options (e.g. when to stop compilation, number of registers
     * to use, ...).
     */
    public CompilerOptions getCompilerOptions() {
        return compilerOptions;
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#add(fr.ensimag.ima.pseudocode.AbstractLine)
     */
    public void add(AbstractLine line) {
        program.add(line);
    }

    /**
     * @see fr.ensimag.ima.pseudocode.IMAProgram#addComment(java.lang.String)
     */
    public void addComment(String comment) {
        program.addComment(comment);
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addLabel(fr.ensimag.ima.pseudocode.Label)
     */
    public void addLabel(Label label) {
        program.addLabel(label);
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addInstruction(fr.ensimag.ima.pseudocode.Instruction)
     */
    public void addInstruction(Instruction instruction) {
        program.addInstruction(instruction);
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addInstruction(fr.ensimag.ima.pseudocode.Instruction,
     * java.lang.String)
     */
    public void addInstruction(Instruction instruction, String comment) {
        program.addInstruction(instruction, comment);
    }
    
    /**
     * Rajoutte une instruction au début du fichier
     */
    public void addInstructionBegin(Instruction instruction) {
    	program.addFirst(instruction);
    }
    
    public void addInstructionBegin(Instruction instruction, String comment) {
    	program.addFirst(instruction, comment);
    }
    
    public void addInstructionAfter(Instruction instruction, int index) {
    	program.addAfter(instruction, index);
    }
    
    public void addInstructionBegin(Instruction instruction, String comment, int index) {
    	program.addAfter(instruction, comment, index);
    }
    
    public int getLastInstructionIndex() {
    	return program.getLength() - 1;
    }
    
    /**
     * @see 
     * fr.ensimag.ima.pseudocode.IMAProgram#display()
     */
    public String displayIMAProgram() {
        return program.display();
    }
    
    private final CompilerOptions compilerOptions;
    private final File source;
    
    private boolean verificationTest = false;
    
    public boolean isVerificationTest() {
    	return verificationTest;
    }
    
    
    /*
     * Nombre de registres disponibles
     */
    private int nombreRegistres;
    /*
     * Premier registre où on stock le résultat des instructions 
     */
//    private GPRegister registerStart = Register.getR(2);
        
    public int getNombreRegistres() {
		return nombreRegistres;
	}
    
    public GPRegister getRegisterStart() {
    	for(int i = 0; i < nombreRegistres - 2; i++) {
    		if(freeRegister[i]) {
    			freeRegister[i] = false;
    			return Register.getR(i + 2);
    		}		
    	}
    	throw new RegisterException("Plus aucun registre n'est disponible");
    }
    
    public void freeRegister(GPRegister r) {
    	freeRegister[r.getNumber() - 2] = true;
    }
    
	/**
     * The main program. Every instruction generated will eventually end up here.
     */
    private final IMAProgram program = new IMAProgram();
 

    /**
     * Run the compiler (parse source file, generate code)
     *
     * @return true on error
     */
    public boolean compile() {

        String sourceFile = source.getAbsolutePath();
        String destFile = null;
        //FAIT : génération du nom du fichier .ass à partir du .deca
        destFile = sourceFile.substring(0, sourceFile.lastIndexOf('/')) + sourceFile.substring(sourceFile.lastIndexOf('/'), sourceFile.lastIndexOf('.')) + ".ass";

        PrintStream err = System.err;
        PrintStream out = System.out;
        LOG.debug("Compiling file " + sourceFile + " to assembly file " + destFile);
        try {
            return doCompile(sourceFile, destFile, out, err);
        } catch (LocationException e) {
            e.display(err);
            return true;
        } catch (DecacFatalError e) {
            err.println(e.getMessage());
            return true;
        } catch (StackOverflowError e) {
            LOG.debug("stack overflow", e);
            err.println("Stack overflow while compiling file " + sourceFile + ".");
            return true;
        } catch (Exception e) {
            LOG.fatal("Exception raised while compiling file " + sourceFile
                    + ":", e);
            err.println("Internal compiler error while compiling file " + sourceFile + ", sorry.");
            return true;
        } catch (AssertionError e) {
            LOG.fatal("Assertion failed while compiling file " + sourceFile
                    + ":", e);
            err.println("Internal compiler error while compiling file " + sourceFile + ", sorry.");
            return true;
        }
    }

    /**
     * Internal function that does the job of compiling (i.e. calling lexer,
     * verification and code generation).
     *
     * @param sourceName name of the source (deca) file
     * @param destName name of the destination (assembly) file
     * @param out stream to use for standard output (output of decac -p)
     * @param err stream to use to display compilation errors
     *
     * @return true on error
     */
    private boolean doCompile(String sourceName, String destName,
            PrintStream out, PrintStream err)
            throws DecacFatalError, LocationException {
    	LOG.info("Nombre de registres : " + nombreRegistres);
        AbstractProgram prog = doLexingAndParsing(sourceName, err); //etape A
        if (prog == null) {
            LOG.info("Parsing failed");
            return true;
        }
        assert(prog.checkAllLocations());
        if(getCompilerOptions().isParse()) {
        	System.out.println(prog.decompile());
        	System.exit(0);
        }
        prog.verifyProgram(this); //etape B
        LOG.info(prog.prettyPrint());
        assert(prog.checkAllDecorations());
        if(getCompilerOptions().isVerification()) {
        	System.exit(0);
        }
        addComment("start main program");
        prog.codeGenProgram(this); //etape C
        addComment("end main program");
        LOG.debug("Generated assembly code:" + nl + program.display());
        LOG.info("Output file assembly file is: " + destName);
        FileOutputStream fstream = null;
        try {
            fstream = new FileOutputStream(destName);
        } catch (FileNotFoundException e) {
            throw new DecacFatalError("Failed to open output file: " + e.getLocalizedMessage());
        }

        LOG.info("Writing assembler file ...");

        program.display(new PrintStream(fstream));
        LOG.info("Compilation of " + sourceName + " successful.");
        
        return false;
    }

    /**
     * Build and call the lexer and parser to build the primitive abstract
     * syntax tree.
     *
     * @param sourceName Name of the file to parse
     * @param err Stream to send error messages to
     * @return the abstract syntax tree
     * @throws DecacFatalError When an error prevented opening the source file
     * @throws DecacInternalError When an inconsistency was detected in the
     * compiler.
     * @throws LocationException When a compilation error (incorrect program)
     * occurs.
     */
    protected AbstractProgram doLexingAndParsing(String sourceName, PrintStream err)
            throws DecacFatalError, DecacInternalError {
        DecaLexer lex;
        try {
            lex = new DecaLexer(CharStreams.fromFileName(sourceName));
        } catch (IOException ex) {
            throw new DecacFatalError("Failed to open input file: " + ex.getLocalizedMessage());
        }
        lex.setDecacCompiler(this);
        CommonTokenStream tokens = new CommonTokenStream(lex);
        DecaParser parser = new DecaParser(tokens);
        parser.setDecacCompiler(this);
        return parser.parseProgramAndManageErrors(err);
    }
    
	public int getCountGB() {
		return countGB;
	}
	public void setCountGB(int countVar) {
		this.countGB = countVar;
	}
    
    public void incrCountGB() {
    	countGB++;
    }

	public Map<Label, String> getErrLblList() {
		return errLblList;
	}
    
    public void addErrLblList(Label newLbl, String err_message) {
    	errLblList.put(newLbl, err_message);
    }
    
    public void codeGenErrLbl() {
    	for (Map.Entry<Label, String> lbl : errLblList.entrySet()) {
    		CompilerInstruction.labelErreurGeneration(this, lbl.getKey(), lbl.getValue());
    	}
    }

    public Label createLabel(String nom) {
    	Label lbl = lblMap.get(nom);
    	if(lbl == null) {
    		lbl = new Label(nom);
    		lblMap.put(nom, lbl);
    	}
    	return lbl;
    }
}
