package core.context;

/**
 * Common context keys shared across all platforms.
 *
 * This file should stay SMALL.
 */
public final class ContextKeys {
	// Key for validation-level context (used by validators)
	public static final ContextKey<ValidationContext> VALIDATION = ContextKey.of("validation", ValidationContext.class);

	private ContextKeys() {
		// TODO Auto-generated constructor stub
	}
}
