package core.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import core.context.registry.ContextViewFactory;

/**
 * Central runtime context used during validation.
 *
 * Responsibilities:
 * - Hold a single immutable source object (Response, Snapshot, etc.)
 * - Lazily create and cache ContextView instances
 * - Delegate view creation to ContextViewFactory
 *
 * This class does NOT:
 * - Parse JSON
 * - Perform validation
 * - Know about API / Contract rules
 */
public class ValidationContext {
	/**
	 * The original runtime source object. This can be a Response, Snapshot, or any
	 * future type.
	 */
	private final Object source;

	/**
	 * Cache of already created context views. Each view is created once and reused.
	 */
	private final Map<Class<? extends ContextView>, ContextView> views = new ConcurrentHashMap<>();

	/**
	 * Creates a validation context with a given source object.
	 *
	 * @param source the original runtime object (must not be null)
	 */
	public ValidationContext(Object source) {
		if (source == null) {
			throw new ContextException("ValidationContext source cannot be null");
		}

		this.source = source;
	}

	/**
	 * Returns the original source object.
	 *
	 * This method should be used sparingly. Prefer accessing data via ContextView
	 * instead.
	 */
	public Object getSource() {
		return source;
	}

	/**
	 * Retrieves a context view of the given type.
	 *
	 * If the view does not exist yet, it will be created lazily using
	 * ContextViewFactory and cached for future use.
	 *
	 * @param viewClass the view class to retrieve
	 * @param <T>       the type of the view
	 * @return the requested context view
	 */
	@SuppressWarnings("unchecked")
	public <T extends ContextView> T getViews(Class<T> viewClass) {
		return (T) views.computeIfAbsent(viewClass, cls -> ContextViewFactory.createView(cls, this));
	}

	/**
	 * Checks whether a given view is already created.
	 *
	 * @param viewClass the view class to check
	 * @return true if the view exists in cache, false otherwise
	 */
	public boolean hasView(Class<? extends ContextView> viewClass) {
		return views.containsKey(viewClass);
	}

}
