package core.context;

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
	
	public <T> void put(ContextKey<T> key, T value) {
		 // Delegate storage to ContextStore
		store.put(key, value);
	}
}
