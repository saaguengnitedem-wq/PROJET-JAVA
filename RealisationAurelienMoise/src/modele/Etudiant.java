package modele;

import exceptions.MatriculeInvalideException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Etudiant extends Personne implements Comparable<Etudiant> {

    // ---------------------------------------------------------------------
    // Attribut spécifique à Etudiant (en plus de ceux hérités de Personne).
    // Final : on n'affecte la référence qu'une seule fois, dans le
    // constructeur. Le CONTENU de la liste évolue, mais pas la liste elle-même.
    // ---------------------------------------------------------------------
    private final List<Note> notes = new ArrayList<>();

    // ---------------------------------------------------------------------
    // Constructeur
    // ---------------------------------------------------------------------

    public Etudiant(String matricule, String prenom, String nom)
            throws MatriculeInvalideException {
        // Appel du constructeur de la classe mère Personne.
        super(matricule, prenom, nom);
    }
    // ---------------------------------------------------------------------
    // Redéfinition de la méthode abstraite de Personne (polymorphisme)
    // ---------------------------------------------------------------------

    @Override
    public String getCategorie() {
        return "Etudiant";
    }

    // ---------------------------------------------------------------------
    // Méthodes métier spécifiques à Etudiant
    // ---------------------------------------------------------------------
    public void ajouterNote(Note note) {
        if (note == null) {
            throw new IllegalArgumentException("La note ajoutée ne peut pas être null.");
        }
        notes.add(note);
    }

    public List<Note> getNotes() {
        return Collections.unmodifiableList(notes);
    }

    /**
     * Calcule la moyenne arithmétique des notes de l'étudiant.
     * Retourne 0.0 si aucune note (évite la division par zéro).
     * @return moyenne entre 0 et 20
     */
    public double calculerMoyenne() {
        if (notes.isEmpty()) {
            return 0.0;
        }
        double somme = 0.0;
        for (Note n : notes) {
            somme += n.getValeur();
        }
        return somme / notes.size();
    }

    /**
     * @return la meilleure note obtenue, ou 0.0 si aucune note
     */
    public double meilleureNote() {
        double max = 0.0;
        for (Note n : notes) {
            if (n.getValeur() > max) {
                max = n.getValeur();
            }
        }
        return max;
    }

    /**
     * @return la plus faible note obtenue, ou 0.0 si aucune note
     */
    public double pireNote() {
        if (notes.isEmpty()) {
            return 0.0;
        }
        double min = 20.0;
        for (Note n : notes) {
            if (n.getValeur() < min) {
                min = n.getValeur();
            }
        }
        return min;
    }

    /**
     * Construit une map "nom du cours -> valeur de la note",
     * utile pour l'affichage. Retournée non modifiable.
     *
     * @return map immuable associant chaque cours à sa note
     */
    public Map<String, Double> getNotesParCours() {
        Map<String, Double> m = new HashMap<>();
        for (Note n : notes) {
            m.put(n.getCours(), n.  getValeur());
        }
        return Collections.unmodifiableMap(m);
    }

    // ---------------------------------------------------------------------
    // Implémentation de Comparable<Etudiant>
    // Tri NATUREL : par moyenne DÉCROISSANTE (meilleur en premier).
    // ---------------------------------------------------------------------

    /**
     Comparaison naturelle entre deux étudiants : par moyenne décroissante.
     */
    @Override
    public int compareTo(Etudiant autre) {
        // On compare autre à this (au lieu de this à autre) pour obtenir
        // un tri DÉCROISSANT sans avoir à appeler .reversed().
        return Double.compare(autre.calculerMoyenne(), this.calculerMoyenne());
    }
}
