package util;

import modele.Etudiant;
import service.GestionnaireEtudiants;

import java.util.List;

/**
 * INTERFACE définissant le contrat de tout afficheur de résultats.
 *
 * <p>L'interface permet de brancher facilement un autre type
 * d'affichage (ex : {@code AfficheurHTML}, {@code AfficheurGraphique},
 * {@code AfficheurSilencieux} pour les tests) sans toucher ni au
 * modèle, ni à la logique métier, ni à Main (hormis une ligne
 * d'instanciation).
 *
 * @author Aurelien et Moise
 * @version 3.0
 */
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
