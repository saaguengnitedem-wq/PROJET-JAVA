package exceptions;

public class MatriculeInvalideException extends DonneeInvalideException {

    public MatriculeInvalideException(String matricule) {
        super("Matricule invalide (null ou vide) : \"" + matricule + "\"");
    }
}
