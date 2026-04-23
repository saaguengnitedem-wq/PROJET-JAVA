package modele;

import exceptions.NoteInvalideException;

public final class Note {
    // ---------------------------------------------------------------------
    // Constantes (l'intervalle de notes valides est une règle métier).
    // ---------------------------------------------------------------------
    public static final double NOTE_MIN = 0.0;

    public static final double NOTE_MAX = 20.0;

    // ---------------------------------------------------------------------
    // Attributs IMMUABLES
    // ---------------------------------------------------------------------

    private final String cours;
    private final double valeur;

    // ---------------------------------------------------------------------
    // Constructeur avec validation
    // ---------------------------------------------------------------------


    public Note(String cours, double valeur) throws NoteInvalideException {

        if (cours == null || cours.trim().isEmpty()) {
            throw new NoteInvalideException(
                    "Le nom du cours ne peut pas être vide ou null.");
        }
        if (valeur < NOTE_MIN || valeur > NOTE_MAX) {
            throw new NoteInvalideException(valeur);
        }

        this.cours  = cours.trim();
        this.valeur = valeur;
    }
    // Getters

    public String getCours()  { return cours;  }
    public double getValeur() { return valeur; }

    @Override
    public String toString() {
        return String.format("%s: %.2f", cours, valeur);
    }
}
