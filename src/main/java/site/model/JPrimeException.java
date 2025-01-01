package site.model;

/**
 * A runtime exception that is thrown only by our code.
 *
 * @author Mihail Stoynov
 */
public class JPrimeException extends RuntimeException {
    public JPrimeException(Throwable cause) {
        super(cause);
    }

    public JPrimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public JPrimeException(String message) {
        super(message);
    }

    public JPrimeException() {
        super();
    }
}
