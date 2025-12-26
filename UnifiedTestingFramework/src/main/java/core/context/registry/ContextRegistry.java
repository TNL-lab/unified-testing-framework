package core.context.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import core.context.ContextKey;
import core.context.ContextKeyFactory;
import core.context.ContextNamespace;

/**
 * Central registry for all Context types in the framework.
 *
 * Responsibilities:
 * - Register context types
 * - Map context type -> namespace
 * - Generate ContextKey without hard-coded strings
 */
public final class ContextRegistry {
     // Maps a context class to its namespace
     private static final Map<Class<?>, ContextNamespace> REGISTRY = new ConcurrentHashMap<>();

     // Prevent instantiation
        private ContextRegistry() {}
    
    // Registers a context type with its namespace
    //This should be called once during framework bootstrap.
    public static <T> void register(Class<?> contextType, ContextNamespace namespace) {
        // Store mapping
        REGISTRY.put(contextType, namespace);
    }

    /**
     * Resolve a ContextKey for a given context type.
     *
     * The key name is derived from the class name, not hard-coded.
     */
    public static <T> ContextKey<T> keyOf(Class<T> contextType) {
        // Infer value type from context type
        ContextNamespace namespace = REGISTRY.get(contextType);
        // Validate registration
        if (namespace == null) {
            throw new IllegalArgumentException("Context type not registered: " + contextType.getName());
        }

        // Convert class name to logical key name (e.g. ApiContext -> apiContext)
        String simpleName = contextType.getSimpleName();
        String loginName = decapitalize(simpleName);

        // Create and return typed ContextKey
        return ContextKeyFactory.ofNamespace(namespace, loginName, contextType);
}
    /**
     * Convert class name to camelCase key.
     */
    private static String decapitalize(String name) {
        // Handle null or empty string
        if (name == null || name.isEmpty()) {
            return name;
        }

        // Handle single character
        char firstChar = name.charAt(0);
        if (Character.isLowerCase(firstChar)) {
            return name;
        }

        // Handle non-empty string
        String restOfName = name.substring(1);
        
        return firstChar + restOfName;
    }
}
