package service;

import modele.Etudiant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Classe GestionnaireEtudiants.
 *
 * <p>Centralise la logique métier portant sur l'ENSEMBLE des étudiants :
 * tri, statistiques globales, recherche. Le principe <b>S</b> de SOLID
 * (Single Responsibility) est respecté : {@link Etudiant} gère UN
 * étudiant, et ce gestionnaire gère la COLLECTION.
 *
 * <p>Le tri par moyenne utilise la méthode
 * {@link Etudiant#compareTo(Etudiant)} (ordre naturel décroissant),
 * ce qui permet d'écrire simplement {@code Collections.sort(liste)}.
 *
 * @author Aurelien et Moise
 * @version 3.0
 */
public class GestionnaireEtudiants {

    /** Liste interne des étudiants gérés. */
    private final List<Etudiant> etudiants;

    /**
     * Construit un gestionnaire à partir d'une liste d'étudiants.
     * La liste reçue est COPIÉE (encapsulation défensive), ce qui
     * protège l'état interne des modifications extérieures.
     *
     * @param etudiants liste initiale des étudiants (null toléré)
     */
    public GestionnaireEtudiants(List<Etudiant> etudiants) {
        this.etudiants = (etudiants == null)
                ? new ArrayList<>()
                : new ArrayList<>(etudiants);
    }

    /**
     * Retourne une nouvelle liste triée par moyenne décroissante.
     * La liste interne n'est pas modifiée.
     *
     * @return copie triée (meilleur étudiant en premier)
     */
    public List<Etudiant> classerParMoyenne() {
        List<Etudiant> copie = new ArrayList<>(etudiants);
        // Utilise l'ordre naturel défini par Etudiant.compareTo().
        Collections.sort(copie);
        return copie;
    }

    /**
     * @return moyenne des moyennes de tous les étudiants (0 si liste vide)
     */
    public double calculerMoyenneGenerale() {
        if (etudiants.isEmpty()) {
            return 0.0;
        }
        double somme = 0.0;
        for (Etudiant e : etudiants) {
            somme += e.calculerMoyenne();
        }
        return somme / etudiants.size();
    }

    /**
     * @return nombre d'étudiants dont la moyenne est ≥ 10
     */
    public int compterAdmis() {
        int count = 0;
        for (Etudiant e : etudiants) {
            if (e.calculerMoyenne() >= 10.0) {
                count++;
            }
        }
        return count;
    }

    /**
     * Recherche un étudiant par son matricule (recherche linéaire,
     * insensible à la casse).
     *
     * @param matricule matricule à chercher (null toléré)
     * @return l'étudiant trouvé, ou null si aucun ne correspond
     */
    public Etudiant rechercherParMatricule(String matricule) {
        if (matricule == null) {
            return null;
        }
        for (Etudiant e : etudiants) {
            if (e.getMatricule().equalsIgnoreCase(matricule)) {
                return e;
            }
        }
        return null;
    }

    /** @return le nombre d'étudiants gérés */
    public int getNombreEtudiants() {
        return etudiants.size();
    }

    /** @return une vue non modifiable de la liste interne */
    public List<Etudiant> getEtudiants() {
        return Collections.unmodifiableList(etudiants);
    }
}
