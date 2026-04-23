package util;

import modele.Etudiant;
import modele.Note;
import service.GestionnaireEtudiants;

import java.util.List;

/**
 * Implémentation console de {@link IAfficheur}.
 *
 * <p>Toutes les méthodes écrivent sur {@code System.out} ; les
 * éventuelles erreurs (étudiant null) sont signalées sur
 * {@code System.err}.
 *
 * <p>Cette classe illustre aussi le POLYMORPHISME : dans
 * {@link #afficherDetailEtudiant(Etudiant)}, on appelle
 * {@code etudiant.getCategorie()}. Cet appel va chercher la bonne
 * implémentation au moment de l'exécution : "Étudiant" si c'est un
 * {@link Etudiant}, et plus tard "Enseignant" ou autre si la
 * hiérarchie de Personne est étendue.
 *
 * @author Aurelien et Moise
 * @version 3.0
 */
public class AfficheurConsole implements IAfficheur {

    @Override
    public void afficherTitre(String titre) {
        String bordure = "=".repeat(titre.length() + 4);
        System.out.println();
        System.out.println(bordure);
        System.out.println("  " + titre);
        System.out.println(bordure);
    }

    @Override
    public void afficherClassement(List<Etudiant> classement) {
        afficherTitre("Classement des etudiants");

        System.out.printf("%-6s %-10s %-25s %-10s%n",
                "Rang", "Matricule", "Nom complet", "Moyenne");
        System.out.println("-".repeat(55));

        int rang = 1;
        for (Etudiant e : classement) {
            System.out.printf("%-6d %-10s %-25s %-10.2f%n",
                    rang,
                    e.getMatricule(),
                    e.getNomComplet(),
                    e.calculerMoyenne());
            rang++;
        }
    }

    @Override
    public void afficherStatistiques(GestionnaireEtudiants gestionnaire) {
        afficherTitre("Statistiques globales");

        int total = gestionnaire.getNombreEtudiants();
        int admis = gestionnaire.compterAdmis();

        System.out.printf("Nombre total d'etudiants : %d%n", total);
        System.out.printf("Nombre d'admis (>=10)    : %d%n", admis);

        double pct = (total > 0) ? (100.0 * admis / total) : 0.0;
        System.out.printf("Taux de reussite         : %.1f %%%n", pct);
        System.out.printf("Moyenne generale         : %.2f / 20%n",
                gestionnaire.calculerMoyenneGenerale());
    }

    @Override
    public void afficherDetailEtudiant(Etudiant etudiant) {
        if (etudiant == null) {
            System.err.println("Etudiant non trouve.");
            return;
        }

        afficherTitre("Detail - " + etudiant.getNomComplet());
        System.out.println("Matricule : " + etudiant.getMatricule());
        // Polymorphisme : getCategorie() est définie dans Personne
        // (abstraite) et implémentée dans Etudiant.
        System.out.println("Categorie : " + etudiant.getCategorie());
        System.out.println("Notes :");
        for (Note n : etudiant.getNotes()) {
            System.out.println("   - " + n);
        }
        System.out.printf("Moyenne : %.2f / 20%n",
                etudiant.calculerMoyenne());
    }
}
