package core.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Central storage for all test execution contexts.
 * Owned and managed by TestContext.
 */
final class ContextStore {
	// Thread-safe map storing context objects by typed keys
	public final Map<ContextKey<?>, Object> store = new ConcurrentHashMap<>();

	// Package-private constructor to restrict creation
	ContextStore() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Store a context instance.
	 *
	 * @param key     typed context key
	 * @param context context object
	 */
	public <T> void put(ContextKey<T> key, T context) {
		store.put(key, context);
	}

	/**
	 * Retrieve a context instance by key.
	 *
	 * @throws ContextException if context not found
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(ContextKey<T> key) {
		Object value = store.get(key);

		if (value == null) {
			throw new ContextException("Context not found: " + key);
		}

		return (T) value;
	}

	/**
	 * Check whether a context exists.
	 */
	public boolean contain(ContextKey<?> key) {
		return store.containsKey(key);
	}

	/**
	 * Clear all stored contexts. Called after each test execution.
	 */
	public void clear() {
		store.clear();

	}
}
