import exceptions.EcritureException;
import exceptions.LectureException;

import io.EcrivainResultats;
import io.IEcrivainResultats;
import io.ILecteurEtudiants;
import io.LecteurCSV;

import modele.Etudiant;

import service.GestionnaireEtudiants;
import service.IStrategieMention;
import service.MentionFrancaise;

import util.AfficheurConsole;
import util.IAfficheur;

import java.util.List;

/**
 * Classe Main - point d'entrée du programme.
 *
 * <p><b>Démonstration d'INJECTION DE DÉPENDANCES</b> : Main ne manipule
 * que des <i>interfaces</i> ({@link ILecteurEtudiants},
 * {@link IEcrivainResultats}, {@link IStrategieMention},
 * {@link IAfficheur}). Les implémentations concrètes ne sont
 * instanciées qu'à un seul endroit, au début de {@link #main(String[])}.
 *
 * <p>C'est l'application directe du principe <b>D</b> de SOLID
 * (<i>Dependency Inversion</i>) : les modules de haut niveau
 * dépendent d'abstractions, jamais de classes concrètes.
 *
 * <p><b>Gestion des exceptions</b> : un bloc try/catch global entoure
 * l'ensemble du traitement. Chaque catégorie d'erreur est traitée
 * séparément, avec un code de sortie différent pour faciliter le
 * diagnostic en production (ex : dans un script shell).
 *
 * <p><b>Usage</b> :
 * <pre>
 *   java Main                                  # fichiers par défaut
 *   java Main entree.csv                       # fichier d'entrée personnalisé
 *   java Main entree.csv sortie.csv            # entrée et sortie personnalisées
 * </pre>
 *
 * @author Aurelien et Moise
 * @version 3.0
 */
public class Main {

    /** Fichier d'entrée par défaut. */
    private static final String FICHIER_ENTREE_DEFAUT = "C:\\Users\\Aurelien Martial\\OneDrive\\Canada\\ONTARIO-OTTAWA\\Cours collègue Ontario et Rafeo\\La cité collegiale\\Hiver 2026\\Programmation avancée IFM30489-0010-H2026\\GestionNotesEtudiantsV3\\RealisationAurelienMoise\\data\\etudiants.csv";

    /** Fichier de sortie par défaut. */
    private static final String FICHIER_SORTIE_DEFAUT = "C:\\Users\\Aurelien Martial\\OneDrive\\Canada\\ONTARIO-OTTAWA\\Cours collègue Ontario et Rafeo\\La cité collegiale\\Hiver 2026\\Programmation avancée IFM30489-0010-H2026\\GestionNotesEtudiantsV3\\RealisationAurelienMoise\\data\\resultats.csv";

    // Codes de sortie pour les différentes catégories d'erreur.
    // Utile si le programme est appelé depuis un script shell.
    private static final int EXIT_OK               = 0;
    private static final int EXIT_ERREUR_LECTURE   = 1;
    private static final int EXIT_ERREUR_ECRITURE  = 2;
    private static final int EXIT_ERREUR_INATTENDUE = 99;

    /**
     * Point d'entrée du programme.
     *
     * @param args [0] = fichier d'entrée (optionnel),
     *             [1] = fichier de sortie (optionnel)
     */
    public static void main(String[] args) {

        // --- 1. Détermination des chemins d'entrée / sortie ---
        String fichierEntree = (args.length > 0) ? args[0] : FICHIER_ENTREE_DEFAUT;
        String fichierSortie = (args.length > 1) ? args[1] : FICHIER_SORTIE_DEFAUT;

        // --- 2. INJECTION DE DÉPENDANCES ---
        // Main ne connaît que des interfaces. Pour changer de format
        // (JSON, base de données, etc.) ou de barème de mention, il
        // suffit de modifier une seule ligne ci-dessous.
        IAfficheur          afficheur  = new AfficheurConsole();
        ILecteurEtudiants   lecteur    = new LecteurCSV();
        IStrategieMention   strategie  = new MentionFrancaise();
        // (on pourrait aussi écrire : new MentionNordAmericaine())
        IEcrivainResultats  ecrivain   = new EcrivainResultats(strategie);

        afficheur.afficherTitre("Gestion de notes des etudiants");
        System.out.println("Fichier d'entree   : " + fichierEntree);
        System.out.println("Fichier de sortie  : " + fichierSortie);
        System.out.println("Bareme de mention  : " + strategie.getNom());

        // --- 3. Traitement principal, entièrement sous try/catch ---
        try {

            // Lecture des données
            List<Etudiant> etudiants = lecteur.lire(fichierEntree);

            if (lecteur.getLignesRejetees() > 0) {
                System.err.println("Avertissement : "
                        + lecteur.getLignesRejetees()
                        + " ligne(s) rejetee(s) lors de la lecture.");
            }

            if (etudiants.isEmpty()) {
                System.err.println("Aucun etudiant valide n'a ete charge. "
                        + "Arret du programme.");
                System.exit(EXIT_ERREUR_LECTURE);
            }

            System.out.println("\n" + etudiants.size()
                    + " etudiant(s) charge(s) avec succes.");

            // Logique métier : tri et statistiques
            GestionnaireEtudiants gestionnaire =
                    new GestionnaireEtudiants(etudiants);
            List<Etudiant> classement = gestionnaire.classerParMoyenne();

            // Affichage
            afficheur.afficherClassement(classement);
            afficheur.afficherStatistiques(gestionnaire);
            if (!classement.isEmpty()) {
                afficheur.afficherDetailEtudiant(classement.get(0));
            }

            // Sauvegarde
            ecrivain.ecrire(classement, fichierSortie);
            System.out.println("\nResultats sauvegardes dans : "
                    + fichierSortie);

            System.exit(EXIT_OK);

        } catch (LectureException e) {
            System.err.println("Erreur de lecture : " + e.getMessage());
            if (e.getCause() != null) {
                System.err.println("Cause : " + e.getCause().getMessage());
            }
            System.exit(EXIT_ERREUR_LECTURE);

        } catch (EcritureException e) {
            System.err.println("Erreur d'ecriture : " + e.getMessage());
            if (e.getCause() != null) {
                System.err.println("Cause : " + e.getCause().getMessage());
            }
            System.exit(EXIT_ERREUR_ECRITURE);

        } catch (Exception e) {
            // Filet de sécurité : tout autre problème imprévu.
            System.err.println("Erreur inattendue : " + e.getMessage());
            e.printStackTrace(System.err);
            System.exit(EXIT_ERREUR_INATTENDUE);
        }
    }
}
