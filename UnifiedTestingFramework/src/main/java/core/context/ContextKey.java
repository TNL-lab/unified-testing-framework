package core.context;

import java.util.Objects;

/**
 * Strongly-typed key used to store and retrieve values from ContextStore.
 *
 * @param <T> type of value associated with this key
 */
public final class ContextKey<T> {
    /**
    * Unique full name of the key (e.g. "api.context", "web.context", etc.)
    */
    private final String name;

    /**
     * Expected type of the value associated with this key
     */
    private final Class<T> type;

    /**
     * Private constructor to enforce factory usage.
     */
    public ContextKey(String name, Class<T> type) {
        this.name = name;
        this.type = type;
    }

    /**
     * Get the name of the key.
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
     * Factory method to create a ContextKey.
     *
     * @param name fully-qualified key name
     * @param type expected value type
     */
    public static <T> ContextKey<T> of(String name, Class<T> type) {
        return new ContextKey<>(name, type);
    }
    /**
     * Returns a string representation of the ContextKey.
     *
     * @return string representation
     */
    @Override
    public String toString() {
        return "ContextKey[name='" + name + "', type=" + type.getName() + "]";
    }

    /**
     * Checks if two ContextKey objects are equal.
     *
     * Two ContextKey objects are equal if they have the same name.
     *
     * @param o object to compare
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof ContextKey)) return false;
        ContextKey<?> other = (ContextKey<?>) o;
        return Objects.equals(name, other.name);
    }

    /**
     * Returns a hash code for this ContextKey.
     *
     * The hash code is based on the name of the key.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
