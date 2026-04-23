package service;

import modele.Etudiant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GestionnaireEtudiants {

    /** Liste interne des étudiants gérés. */
    private final List<Etudiant> etudiants;

    public GestionnaireEtudiants(List<Etudiant> etudiants) {
        this.etudiants = (etudiants == null)
                ? new ArrayList<>()
                : new ArrayList<>(etudiants);
    }

    public List<Etudiant> classerParMoyenne() {
        List<Etudiant> copie = new ArrayList<>(etudiants);
        // Utilise l'ordre naturel défini par Etudiant.compareTo().
        Collections.sort(copie);
        return copie;
    }

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
     * Recherche un étudiant par son matricule (recherche linéaire, insensible à la casse).
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

    /** retourne le nombre d'étudiants gérés */
    public int getNombreEtudiants() {
        return etudiants.size();
    }

    /** retourne une vue non modifiable de la liste interne */
    public List<Etudiant> getEtudiants() {
        return Collections.unmodifiableList(etudiants);
    }
}
