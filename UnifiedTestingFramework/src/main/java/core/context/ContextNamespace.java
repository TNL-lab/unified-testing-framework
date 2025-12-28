package core.context;
/**
 * Defines logical namespaces for context keys.
 *
 * A namespace:
 * - Prevents key name collisions
 * - Clearly indicates ownership and scope
 * - Allows future extension (AI, Performance, Security, etc.)
 * - Has NO runtime meaning
 */
public enum ContextNamespace {

    /**
     * Execution-level, platform-agnostic namespace.
     * Used for execution-wide context.
    */
    ROOT,

    /**
     * API-related context namespace.
     */
    API,

    /**
     * Web UI-related context namespace.
     */
    WEB,

    /**
     * Mobile-related context namespace.
     */
    MOBILE;

    /**
     * Convert enum name to lowercase prefix.
     *
     * Example:
     * WEB -> "web"
     */
    public String prefix() {
        return name().toLowerCase();
    }
}