package exceptions;

public class DonneeInvalideException extends Exception {

    /**
     Construit une exception avec un message explicatif.
     */
    public DonneeInvalideException(String message) {
        super(message);
    }

    /**
     * Construit une exception avec un message et la cause d'origine.
     */
    public DonneeInvalideException(String message, Throwable cause) {
        super(message, cause);
    }
}
