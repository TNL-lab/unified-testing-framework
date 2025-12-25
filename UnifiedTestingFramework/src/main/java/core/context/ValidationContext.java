package core.context;

/**
 * Holds validation-related runtime data.
 *
 * This context exists only during validation phase
 * and should not leak into execution-level context.
 */
public class ValidationContext {
	// Indicates whether strict validation mode is enabled
    private boolean strictMode;

    public boolean isStrictMode() {
        return strictMode;
    }

    public void setStrictMode(boolean strictMode) {
        this.strictMode = strictMode;
    }
}
