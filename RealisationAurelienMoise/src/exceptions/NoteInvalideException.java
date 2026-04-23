package exceptions;

import modele.Note;

public class NoteInvalideException extends DonneeInvalideException {

    /**
     Construit l'exception à partir d'une valeur hors bornes.
     */
    public NoteInvalideException(double valeur) {
        super(String.format(
                "Valeur de note hors des bornes autorisées [%.1f ; %.1f] : %.2f",
                Note.NOTE_MIN, Note.NOTE_MAX, valeur));
    }

    /**
     Construit l'exception avec un message personnalisé.
     */
    public NoteInvalideException(String message) {
        super(message);
    }
}
