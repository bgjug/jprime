package site.model;

/**
 * A runtime exception that is thrown only by our code.
 *
 * @author Mihail Stoynov
 */
public class JprimeException extends RuntimeException {
    public JprimeException(Throwable cause) {
        super(cause);
    }

    public JprimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public JprimeException(String message) {
        super(message);
    }

    public JprimeException() {
        super();
    }
}
