package util;

import modele.Etudiant;
import service.GestionnaireEtudiants;

import java.util.List;


public interface IAfficheur {

    /** Affiche un titre encadré. */
    void afficherTitre(String titre);

    /** Affiche le classement sous forme de tableau. */
    void afficherClassement(List<Etudiant> classement);

    /** Affiche les statistiques globales de la promotion. */
    void afficherStatistiques(GestionnaireEtudiants gestionnaire);

    /** Affiche le détail d'un seul étudiant (toutes ses notes). */
    void afficherDetailEtudiant(Etudiant etudiant);
}
