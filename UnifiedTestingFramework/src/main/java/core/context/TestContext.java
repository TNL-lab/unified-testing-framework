package core.context;

import core.context.registry.ContextRegistry;

/**
 * Root context object for a single test execution.
 *
 * Responsibilities:
 * - Own and manage ContextStore lifecycle
 * - Provide typed access to stored contexts
 * - Act as the single source of truth for test state
 *
 * This class MUST remain tool-agnostic.
 */
public class TestContext {
	 // Central storage for all test-related contexts
	private final ContextStore store;
	
    /**
     * Create a new TestContext instance.
     * Usually initialized at the beginning of each test.
     */
	public TestContext() {
		 // Initialize empty context store
		this.store= new ContextStore();
	}

	 /**
     * Put a context instance into store.
     */
		public <T> void put(T context) {
		// Infer context type
		Class<T> contextClass = (Class<T>) context.getClass();
		// Resolve ContextKey from registry
		ContextKey<T> key = ContextRegistry.keyOf(contextClass);
		// Store context instance
		store.put(key, context);
	}

    /**
     * Retrieve a context by type.
     */
	public <T> T get(Class<T> contextType) {
		// Resolve ContextKey from registry
		ContextKey<T> key = ContextRegistry.keyOf(contextType);
		// Retrieve context instance
		return store.get(key);
	}

    /**
     * Check if a context exists.
     */
	public <T> boolean contains(Class<T> contextType) {
		// Resolve ContextKey from registry
		ContextKey<T> key = ContextRegistry.keyOf(contextType);
		// Check existence
		return store.contains(key);
	}

	/**
     * Clear all stored contexts.
     * Called after test execution finishes.
     */
	public void clear() {
		// Clear all stored contexts
		store.clear();
	}

	 /**
     * Check whether a context exists.
     *
     * @param key typed context key
     * @return true if context exists
     */
	public boolean has(ContextKey<?> key) {
		// Check existence
		return store.contains(key);
	}
}
