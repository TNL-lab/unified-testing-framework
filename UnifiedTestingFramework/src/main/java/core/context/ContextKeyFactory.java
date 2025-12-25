package core.context;

/**
 * Centralized factory for creating ContextKey instances.
 *
 * Prevents string duplication and typo errors.
 */
public final class ContextKeyFactory {

	private ContextKeyFactory() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Centralized key creation logic.
	 *
	 * This is the SINGLE place that defines how context keys are named.
	 */
	private static <T> ContextKey<T> create(ContextNamespace namespace, String name, Class<T> type) {
		// Build fully-qualified key name (e.g. "api.context")
		String keyName = namespace.prefix() + "." + name;

		// Create typed ContextKey instance
		return ContextKey.of(keyName, type);
	}

	/**
	 * Create a dyanmic context key(used for plugin, external module)
	 */
	public static <T> ContextKey<T> ofNamespace(ContextNamespace namespace, String name, Class<T> type) {
		return create(namespace, name, type);
	}

	/**
	 * Create a api-level context key.
	 */
	public static <T> ContextKey<T> api(String name, Class<T> type) {
		return create(ContextNamespace.API, name, type);
	}

	/**
	 * Create a web-level context key.
	 */
	public static <T> ContextKey<T> web(String name, Class<T> type) {
		return create(ContextNamespace.WEB, name, type);
	}

	/**
	 * Create a mobile-level context key.
	 */
	public static <T> ContextKey<T> mobile(String name, Class<T> type) {
		return create(ContextNamespace.MOBILE, name, type);
	}

	/**
	 * Create a root-level context key.
	 */
	public static <T> ContextKey<T> root(String name, Class<T> type) {
		return create(ContextNamespace.ROOT, name, type);
	}

	/**
	 * Create a validation-level context key.
	 */
	public static <T> ContextKey<T> validation(String name, Class<T> type) {
		return create(ContextNamespace.VALIDATION, name, type);
	}
}
