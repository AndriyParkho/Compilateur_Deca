	package fr.ensimag.deca;

import java.io.File;


import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.EnvironmentType;
import org.apache.log4j.Logger;

/**
 * Main class for the command-line Deca compiler.
 *
 * @author gl10
 * @date 01/01/2021
 */
public class DecacMain {
    private static Logger LOG = Logger.getLogger(DecacMain.class);
    
    public static void main(String[] args) {
        // example log4j message.
        LOG.info("Decac compiler started");
        boolean error = false;
        final CompilerOptions options = new CompilerOptions();
        try {
            options.parseArgs(args);
        } catch (CLIException e) {
            System.err.println("Error during option parsing:\n"
                    + e.getMessage());
            options.displayUsage();
            System.exit(1);
        }
        if (options.getPrintBanner()) {
            System.out.println("-----------< Projet GL >-----------");
            System.out.println("             Groupe  2");
            System.out.println("             Equipe 10");
            System.out.println("        Louis Jézéquel-Royer");
            System.out.println("         Andriy Parkhomenko");
            System.out.println("          Mohamed Ali Lagha");
            System.out.println("            Etienne Gacel");
            System.out.println("            Bastien Fabre");
            System.out.println("------------------------------------");
            System.exit(0);
            
        }
        if (options.getSourceFiles().isEmpty()) {
            System.out.println("---------------------------< Options disponibles >--------------------------------");
            System.out.println("-b       (banner)       : affiche une bannière indiquant le nom de l’équipe. ");
            System.out.println("-p       (parse)        : arrête decac après l’étape de construction de l’arbre, et\n			  affiche la décompilation de ce dernier (i.e. s’il n’y a\n			  qu’un fichier source à compiler, la sortie doit être un\n			  programme deca syntaxiquement correct).");
            System.out.println("-v       (verification) : arrête decac après l’étape de vérifications (ne produit\n			  aucune sortie en l’absence d’erreur).");
            System.out.println("-n       (no check)     : supprime les tests à l’exécution spécifiés dans les points\n			  11.1 et 11.3 de la sémantique de Deca.");
            System.out.println("-r X     (registers)    : limite les registres banalisés disponibles à R0 ... R{X-1},\n			  avec 4 <= X <= 16.");
            System.out.println("-d       (debug)        : active les traces de debug. Répéter l’option plusieurs fois\n			  pour avoir plus de traces. ");
            System.out.println("-P       (parallel)     : s’il y a plusieurs fichiers sources, lance la compilation\n			  des fichiers en parallèle (pour accélérer la compilation)");
            System.out.println("----------------------------------------------------------------------------------");
            System.exit(0);
        }
        if (options.getParallel()) {
            // A FAIRE : instancier DecacCompiler pour chaque fichier à
            // compiler, et lancer l'exécution des méthodes compile() de chaque
            // instance en parallèle. Il est conseillé d'utiliser
            // java.util.concurrent de la bibliothèque standard Java.
        	for(File source : options.getSourceFiles()) {
        		DecacCompiler compiler = new DecacCompiler(options, source);
        		Runnable myRunnable = () -> {compiler.compile();};
        		(new Thread(myRunnable)).start();
//        		System.out.println("Fichier source : "+source);
        		
        	}
        } else {
            for (File source : options.getSourceFiles()) {
                DecacCompiler compiler = new DecacCompiler(options, source);
                if (compiler.compile()) {
                    error = true;
                }
            }
        }
        System.exit(error ? 1 : 0);
    }
    
  
}
