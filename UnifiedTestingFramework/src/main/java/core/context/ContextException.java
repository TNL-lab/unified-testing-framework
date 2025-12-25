package core.context;

/**
 * Custom runtime exception for all context-related errors.
 *
 * This is used when:
 * - A requested view is not supported
 * - A view cannot be created due to missing dependencies
 * - Invalid source object is provided
 */
public class ContextException extends RuntimeException{
	
	public ContextException(String msg) {
		super(msg);
	}
	
	public ContextException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
