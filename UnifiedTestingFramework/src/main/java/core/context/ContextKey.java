package core.context;

/**
 * Typed key used to store and retrieve values from TestContext.
 *
 * This avoids hard-coded string keys and provides compile-time type safety.
 *
 * @param <T> Type of value associated with this key
 */
public final class ContextKey<T> {
	// Unique key name (e.g. "api", "api.snapshot")
	private final String name;

	// Expected type of the stored value
	private final Class<T> type;

	/**
	 * Private constructor to enforce usage through factory method.
	 *
	 * @param name logical name of the context key
	 */
	private ContextKey(String name, Class<T> type) {
		this.name = name;
		this.type = type;
	}

	/**
	 * Factory method to create a new typed ContextKey.
	 *
	 * @param name logical name of the key
	 * @return typed ContextKey instance
	 */
	public static <T> ContextKey<T> of(String name, Class<T> type) {
		return new ContextKey<T>(name, type);
	}

	/**
	 * Returns expected value type.
	 */
	public Class<T> type() {
		return type;
	}

	/**
	 * Returns key name (useful for logging/debugging).
	 */
	@Override
	public String toString() {
		return "ContextKey[" + name + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) 
			return true;
		if (!(obj instanceof ContextKey<?> other)) return false; 
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}
}