package core.context;

import java.util.Objects;

/**
 * Strongly-typed key used to store and retrieve values from ContextStore.
 *
 *  A ContextKey consists of:
 * - Logical namespace
 * - Stable name
 * - Expected value type
 *
 * @param <T> type of value associated with this key
 */
public final class ContextKey<T> {
    /**
    * Fully-qualified key name (e.g. "api.context", "web.context", etc.)
    */
    private final String name;

    /**
     * Logical namespace of the key
     */
    private final ContextNamespace namespace;

    /**
     * Expected type of the value associated with this key
     */
    private final Class<T> type;

    /**
     * Private constructor to enforce factory usage.
     */
    public ContextKey(String name, ContextNamespace namespace, Class<T> type) {
        // Assign key name
        this.name = name;
        // Assign namespace
        this.namespace = namespace;
        // Assign expected value type
        this.type = type;
    }

    /**
     * Get the fully-qualified key name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the expected type of the value associated with this key.
     */
    public Class<T> getType() {
        return type;
    }

    /**
     * Get the logical namespace of the key.
     */
    public ContextNamespace getNamespace() {
        return namespace;
    }

    /**
     * Factory method to create a ContextKey.
     *
     * @param name      fully-qualified key name
     * @param namespace logical namespace
     * @param type      expected value type
     * @return new ContextKey instance
     */
    public static <T> ContextKey<T> of(String name, ContextNamespace namespace, Class<T> type) {
                // Validate key name
        if (name == null || name.isBlank()) {
            throw new ContextException("ContextKey name must not be null or blank");
        }

        // Validate namespace
        if (namespace == null) {
            throw new ContextException("ContextKey namespace must not be null");
        }

        // Validate type
        if (type == null) {
            throw new ContextException("ContextKey type must not be null");
        }

        // Create new immutable ContextKey
        return new ContextKey<>(name, namespace,type);
    }
    /**
     * Returns a string representation of the ContextKey.
     *
     * @return string representation
     */
    @Override
    public String toString() {
        return "ContextKey[" + namespace.prefix() + ":" + name + "]";
    }

    /**
     * ContextKeys are equal if their name and namespace match.
     *
     * Type is intentionally NOT part of equality
     * to avoid generic type erasure issues.
     *
     * @param o object to compare
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        // Same reference check
        if (o == this) return true;

        // Type check
        if (!(o instanceof ContextKey<?> other)) return false;

        // Compare name and namespace
        return Objects.equals(name, other.name)
                && namespace == other.namespace;
    }

    /**
     * Returns a hash code for this ContextKey.
     *
     * The hash code is based on name and namespace of the key.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, namespace);
    }
}
