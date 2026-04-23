package modele;

import exceptions.MatriculeInvalideException;

import java.util.Objects;

public abstract class Personne {

    // ---------------------------------------------------------------------
    // Attributs protégés ET final : visibles par les sous-classes (Etudiant,
    // et demain Enseignant), mais plus modifiables une fois construits.
    // ---------------------------------------------------------------------
    protected final String matricule;

    protected final String prenom;

    protected final String nom;

    // ---------------------------------------------------------------------
    // Constructeur protégé : seules les sous-classes peuvent l'appeler
    // via super(). Empêche d'instancier Personne directement.
    // ---------------------------------------------------------------------
    protected Personne(String matricule, String prenom, String nom)
            throws MatriculeInvalideException {

        // Validation FAIL-FAST : on refuse de construire un objet incohérent.
        if (matricule == null || matricule.trim().isEmpty()) {
            throw new MatriculeInvalideException(matricule);
        }

        this.matricule = matricule.trim(); // Supprime les espaces
        this.prenom    = (prenom == null) ? "" : prenom.trim(); //opérateur ternaire
        this.nom       = (nom    == null) ? "" : nom.trim();
    }

    // Méthode ABSTRAITE : chaque sous-classe DOIT la redéfinir.
    public abstract String getCategorie();

    // Getters

    public String getMatricule() { return matricule; }
    public String getPrenom()    { return prenom;    }
    public String getNom()       { return nom;       }

    public String getNomComplet() {
        if (prenom.isEmpty()) return nom;
        if (nom.isEmpty())    return prenom;
        return prenom + " " + nom;
    }

    // ---------------------------------------------------------------------
    // equals / hashCode basés sur le matricule (identité logique)
    // ---------------------------------------------------------------------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Personne)) return false;
        return matricule.equals(((Personne) o).matricule);
    }

     @Override
    public int hashCode() {
        return Objects.hash(matricule);
    }

    @Override
    public String toString() {
        return String.format("%s{matricule=%s, nom=%s}",
                getCategorie(), matricule, getNomComplet());
    }
}
