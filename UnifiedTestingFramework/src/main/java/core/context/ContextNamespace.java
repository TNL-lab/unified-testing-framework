package core.context;
/**
 * Defines logical namespaces for context keys.
 *
 * A namespace:
 * - Prevents key name collisions
 * - Clearly indicates ownership and scope
 * - Allows future extension (AI, Performance, Security, etc.)
 */
public enum ContextNamespace {

    ROOT, // Execution-level, platform-agnostic
    API, // API testing contexts
    WEB, // Web UI testing contexts
    MOBILE; // Mobile testing contexts

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
