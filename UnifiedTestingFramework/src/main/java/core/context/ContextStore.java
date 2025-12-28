package core.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Internal Thread-safe storage for all context data.
 *
 * Responsibilities:
 * - Stores values by ContextKey
 * - Enforces type safety at retrieval
 * - Is cleared after each test execution
 * - Be thread-safe for parallel test execution
 */
final class ContextStore {
    // Internal concurrent map storing context values
    private final Map<ContextKey<?>, Object> store = new ConcurrentHashMap<>();

    /**
     * Put a value into the context store.
     *
     * @param key   The context key
     * @param value The value to store
     * @param <T>   The type of the value
     */
    <T> void put(ContextKey<T> key, T value) {
        // Validate key to avoid silent corruption
        if (key == null) {
            throw new ContextException("ContextKey must not be null");
        }

        // Put the value into the store
        store.put(key, value);
    }

    /**
     * Retrieve a value from the context store.
     *
     * @param key The context key
     * @param <T> The type of the value
     * @return The stored value, or null if not present
     */

     @SuppressWarnings("unchecked")
    <T> T get(ContextKey<T> key) {
        // Validate key to avoid silent corruption
        if (key == null) {
            throw new ContextException("ContextKey must not be null");
        }

        // Retrieve the raw value from the store
        Object value = store.get(key);

        // Enforce type safety
        if (value == null) {
            throw new ContextException("Context not found:" + key.getName());
        }

        // Check if the value matches the expected type
        if (!key.getType().isInstance(value)) {
            throw new ContextException("Type mismatch for key: " + key.getName() +
                    ". Expected: " + key.getType().getName() +
                    ", Found: " + value.getClass().getName());
        }

        // Return the value cast to the expected type
        return (T) value;
    }

    /**
     * Check if a key exists in the context store.
     *
     * @param key The context key
     * @return true if the key exists, false otherwise
     */
    boolean contains(ContextKey<?> key) {
        // Null-safe containment check
        return key != null && store.containsKey(key);
    }

    /**
     * Remove all stored context data.
     *
     * Typically called after test execution.
     */
    void clear() {
        store.clear();
    }
}