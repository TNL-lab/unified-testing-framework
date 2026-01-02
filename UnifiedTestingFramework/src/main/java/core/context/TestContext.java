package core.context;

import core.context.registry.ContextRegistry;

/**
 * Runtime container for all context data during a test execution.
 *
 * Responsibilities:
 * - Provide typed access via ContextKey or Context class
 * - Fail fast when required context is missing
 * - SINGLE entry point to context storage
 * - Delegate storage & type-safety to ContextStore
 * - Resolve context keys from context type
 *
 */
public final class TestContext {
    // Internal context storage
    private final ContextStore store = new ContextStore();

    /**
     * Store a context value with the context key.
     *
     * @param key   The context key
     * @param value The value to store
     * @param <T>   The type of the value
     */
    public <T> void put(ContextKey<T> key, T value) {
       // Delegate storage to ContextStore
        store.put(key, value);
    }

    /**
     * Store a context value using class type.
     *
     * @param value The value to store
     * @param <T>   The type of the value
     */
    public <T> void put(Class<T> clazz, T value) {
        // Get the context key for the value
        ContextKey<T> key = resolveKey(clazz);

       // Store the value with the context key
       put(key, value);
    }

    /**
     * Store a context value with the context key, or fail fast if the value is null.
     *
     * @param key   the context key
     * @param value the value to store
     * @param <T>   the type of the value
     * @throws ContextException if the value is null
     */
    public <T> void putRequired(ContextKey<T> key, T value) {
        // Fail fast if missing
        value = requireValue(value, "Context value must not be null for key: " + key.getName());

        // Store the value with the context key
        put(key, value);
    }

    /**
     * Store a context value using class type, or fail fast if the value is null.
     *
     * @param clazz the class for which to resolve the ContextKey
     * @param value the value to store
     * @param <T>   the type of the value
     * @throws ContextException if the value is null
     */
    public <T> void putRequired(Class<T> clazz, T value) {
         // Get the context key for the value
        ContextKey<T> key = resolveKey(clazz);

        // Store the value with the context key
        putRequired(key, value);
    }

    /**
     * Retrieve a context value.
     *
     * @param key The context key
     * @param <T> The type of the value
     * @return The stored value, or null if not found 
     */
    public <T> T get(ContextKey<T> key) {
        // Delegate retrieval to ContextStore
        return store.get(key);
    }

    /**
     * Get a context value using class type.
     *
     * @param clazz The class of the value
     * @param <T>   The type of the value
     * @return The stored value
     */
    public <T> T get(Class<T> clazz) {
        // Get the context key from class via ContextRegistry
        ContextKey<T> key = resolveKey(clazz);

        // Delegate retrieval to ContextStore
        return store.get(key);
    }

    /**
     * Check if a key exists in the test context store.
     *
     * @param key The context key
     * @return True if the key exists, false otherwise
     */
    public boolean contains(ContextKey<?> key) {
        // Delegate containment check
        return store.contains(key);
    }

    /**
     * Clear all context data.
     *
     *  Usually invoked by test lifecycle hooks.
     */
    public void clear() {
        // Delegate cleanup to ContextStore
        store.clear();
    }

    /**
     * Retrieve a required context value.
     *
     * Throws ContextException if the value is missing.
     *
     * @param key typed context key
     * @param <T> expected value type
     * @return stored value (never null)
     */
    public <T> T require(ContextKey<T> key) {
        // Retrieve value
        T value = get(key);

        // Fail fast if missing
        value = requireValue(value, "Required context key missing: " + key);

        // Return the value
        return value;
    }

    /**
     * Get a value from the store using class type.
     * Fail-fast if value is missing.
     */
    public <T> T require(Class<T> clazz) {
        // Retrieve value
        T value = get(clazz);

        // Fail fast if missing
        value = requireValue(value, "Required context not found for type: " + clazz.getName());

        // Return the value
        return value;
    }

    /**
     * Resolve a ContextKey for the given class using ContextRegistry.
     *
     * @param clazz the class for which to resolve the ContextKey
     * @return the resolved ContextKey
     * @throws ContextException if the class is not registered
     */
    @SuppressWarnings("unchecked")
    private <T> ContextKey<T> resolveKey(Class<?> clazz) {
        return ContextRegistry.keyOf((Class<T>)clazz);
    }

    /**
     * Requires a non-null value with the given error message.
     *
     * Throws ContextException if the value is null.
     *
     * @param value the value to require
     * @param errorMessage the error message to use if the value is null
     * @return the value if not null
     * @throws ContextException if the value is null
     */
    private <T> T requireValue(T value, String errorMessage) {
        // Fail fast if missing
        if (value == null) {
            throw new ContextException(errorMessage);
        }

        // Return the value
        return value;
    }
}
