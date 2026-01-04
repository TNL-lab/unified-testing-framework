package core.context.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import core.context.ContextKey;
import core.context.ContextKeyFactory;
import core.context.ContextNamespace;
import core.context.support.ContextPreconditions;

/**
 * Central registry for all Context types in the framework.
 *
 * Responsibilities:
 * - Register Context class with logical namespace
 * - Map context type -> namespace
 * - Generate ContextKey from Context class without hard-coded strings
 * - API context types should register via ApiContextModule
 */
public final class ContextRegistry {

    /**
     * Internal registry mapping a context class to its logical namespace. (e.g. ApiContext -> ContextNamespace.API)
    */
    private static final Map<Class<?>, ContextNamespace> REGISTRY = new ConcurrentHashMap<>();

    // Private constructor to prevent instantiation
    private ContextRegistry() {}

    /**
     * Registers a context type with its corresponding namespace.
     *
     * @param contextType the context type to register
     * @param contextNamespace the namespace associated with the context type
     */
    public static void register(Class<?> contextType, ContextNamespace contextNamespace) {
        // Fail fast if context type is null
        ContextPreconditions.requireNonNull(contextType, "Context type must not be null");

        // Fail fast if namespace is null
        ContextPreconditions.requireNonNull(contextNamespace, "Context namespace must not be null");

        // Store the mapping in the registry
        REGISTRY.put(contextType, contextNamespace);
    }

    /**
     * Get the namespace associated with a given context type.
     *
     * @param contextType the context type for which to retrieve the namespace
     * @return the associated ContextNamespace
     * @throws ContextException if the context type is not registered
     */
    public static ContextNamespace getNamespace(Class<?> contextType) {
        // Lookup namespace from registry
        ContextNamespace namespace = REGISTRY.get(contextType);

        // Error if not registered
        ContextPreconditions.requireNonNull(namespace, "Unregistered context type: " + contextType.getName());

        // Return the namespace
        return namespace;
    }

    /**
     * Resolve a ContextKey for the given context type.
     * E.g. ApiContext.class -> ContextKey< ApiContext >
     *
     * @param contextType the context type for which to generate the ContextKey
     * @return the generated ContextKey
     * @throws ContextException if the context type is not registered
     */
    @SuppressWarnings("unchecked")
    public static <T> ContextKey<T> keyOf(Class<T> contextType) {
       // Lookup namespace from registry
        ContextNamespace namespace = getNamespace(contextType);

        //Convert class name to logical key name (e.g. ApiContext -> apiContext)
        String simpleName = contextType.getSimpleName();
        String keyName = decapitalize(simpleName);

        // Create and return the ContextKey
        return ContextKeyFactory.ofNamespace(namespace, keyName, contextType);
    }

    /**
     * Converts the first character of the given name to lowercase.
     *
     * @param name the name to decapitalize
     * @return the decapitalized name
     */
    private static String decapitalize(String name) {
        // Handle null or empty string
        if (name == null || name.isEmpty()) {
            return name;
        }

        //Get the first character
        char first = name.charAt(0);
        // If already lowercase, return as is
        if (Character.isLowerCase(first)) {
            return name;
        }

        // Convert first character to lowercase and concatenate with the rest of the string
        String result = Character.toLowerCase(first) + name.substring(1);

        return result;
    }

    /**
     * Checks if a context type is registered in the registry.
     *
     * @param contextType the context type to check
     * @return true if registered, false otherwise
     */
    public static boolean isRegistered(Class<?> contextType) {
        // Lookup context type from registry
        return REGISTRY.containsKey(contextType);
    }

	/**
     * Clears all registered context types from the registry (for testing purposes)
     * For test teardown only.
     *
     * ⚠ Framework-internal use only.
     * Intended for test lifecycle cleanup.
     */
    public static void clear() {
        // Clear the registry
        REGISTRY.clear();
    }
}
