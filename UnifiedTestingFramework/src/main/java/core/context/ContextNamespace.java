package core.context;

/**
 * Defines logical namespaces for context keys.
 *
 * A namespace prevents key collisions and
 * clearly indicates ownership and scope.
 */
public enum ContextNamespace {
	ROOT, // Execution-level, platform-agnostic
	WEB, // Web UI-specific contexts
	API, // API-specific contexts
	MOBILE, // Mobile-specific contexts
	VALIDATION // Validation-phase contexts
	// Namespace reserved for future contexts (e.g. AI, SECURITIES,etc)
	; 
	
    /**
     * Convert namespace to lowercase prefix.
     */
	public String prefix() {
		return name().toLowerCase();
	}
}
