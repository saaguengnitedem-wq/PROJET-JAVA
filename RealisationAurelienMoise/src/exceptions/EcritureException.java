package exceptions;

public class EcritureException extends Exception {

    public EcritureException(String message) {
        super(message);
    }

    public EcritureException(String message, Throwable cause) {
        super(message, cause);
    }
}
