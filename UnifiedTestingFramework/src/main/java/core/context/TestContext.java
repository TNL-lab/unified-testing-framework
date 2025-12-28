package core.context;
/**
 * Runtime container for all context data during a test execution.
 *
 * Responsibilities:
 * - Provide typed access to context values
 * - Fail fast when required context is missing
 * - Act as the SINGLE entry point to context storage
 */
public final class TestContext {
    // Internal context storage
    private final ContextStore store = new ContextStore();

    /**
     * Store a context value.
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
        // Retrieve value from store
        T value = store.get(key);

        // Fail fast if missing
        if (value == null) {
            throw new ContextException("Required context key missing: " + key);
        }

        // Return the value
        return value;
    }
}
