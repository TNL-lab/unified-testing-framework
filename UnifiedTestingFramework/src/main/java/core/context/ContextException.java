package core.context;
/**
 * Base runtime exception for all context-related errors.
 *
 * This exception is thrown when:
 * - A required context is missing
 * - A context key is invalid
 * - Context lifecycle is violated
 */
public class ContextException extends RuntimeException {
    /**
     * Create a new ContextException with message only.
     *
     * @param message error description
     */
    public ContextException(String message) {
        super(message);
    }

    /**
     * Create a new ContextException with message and root cause.
     *
     * @param message error description
     * @param cause   underlying cause of the exception
     */
    public ContextException(String message, Throwable cause) {
        super(message, cause);
    }
}

