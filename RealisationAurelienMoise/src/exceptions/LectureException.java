package exceptions;

public class LectureException extends Exception {

    public LectureException(String message) {
        super(message);
    }

    public LectureException(String message, Throwable cause) {
        super(message, cause);
    }
}
