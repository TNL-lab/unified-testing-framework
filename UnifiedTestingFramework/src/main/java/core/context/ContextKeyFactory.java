package core.context;
/**
 * Centralized factory for creating ContextKey instances.
 *
 * This class:
 * - Prevents string duplication
 * - Avoids typo errors
 * - Enforces naming convention
 */
public final class ContextKeyFactory {
    // Private constructor to prevent instantiation
    private ContextKeyFactory() {
    }

    /**
     * Creates a ContextKey instance with the given namespace, name, and type. 
     * 
     * @param namespace logical namespace for the key
     * @param name          unique name for the key within the namespace
     * @param type         expected type for the value associated with this key
     * @return a newly-created ContextKey instance
     */
    private static <T> ContextKey<T> create(ContextNamespace namespace, String name, Class<T> type) {
        // Construct the fully-qualified key name (e.g. "api.context")
        String keyName = namespace.prefix() + "." + name;

        // Create and return the ContextKey
        return ContextKey.of(keyName, type);
    }


    /**
     * Creates a ContextKey instance with the ROOT namespace.
     *
     * @param name  unique name for the key within the namespace
     * @param type  expected type for the value associated with this key
     * @return a newly-created ContextKey instance
     */
    public static <T> ContextKey<T> root(String name, Class<T> type) {
       // Delegate to the private create method with ROOT namespace
        return create(ContextNamespace.ROOT, name, type);
    }

    /**
     * Creates a ContextKey instance with the API namespace.
     *
     * @param name  unique name for the key within the namespace
     * @param type  expected type for the value associated with this key
     * @return a newly-created ContextKey instance
     */
    public static <T> ContextKey<T> api(String name, Class<T> type) {
        // Delegate to the private create method with API namespace
        return create(ContextNamespace.API, name, type);
    }

    /**
     * Creates a ContextKey instance with the WEB namespace.
     *
     * @param name  unique name for the key within the namespace
     * @param type  expected type for the value associated with this key
     * @return a newly-created ContextKey instance
     */
    public static <T> ContextKey<T> web(String name, Class<T> type) {
        // Delegate to the private create method with WEB namespace
        return create(ContextNamespace.WEB, name, type);
    }
    /**
     * Creates a dynamic ContextKey instance with a custom or plugin namespace.
     *
     * @param namespace logical namespace for the key
     * @param name      unique name for the key within the namespace
     * @param type      expected type for the value associated with this key
     * @return a newly-created ContextKey instance
     */
    public static <T> ContextKey<T> ofNamespace(ContextNamespace namespace,String name, Class<T> type) {
       // Delegate to the private create method with the specified namespace
        return create(namespace, name, type);
    }

}