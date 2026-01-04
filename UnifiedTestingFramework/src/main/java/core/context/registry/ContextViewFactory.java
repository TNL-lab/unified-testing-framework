package core.context.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

import core.context.ContextException;
import core.context.view.ContextView;

/**
 * Factory responsible for resolving ContextView instances from Context objects.
 *
 * Responsibilities:
 * - Register default ContextView for a given Context type
 * - Register specialized view contracts (RawJsonView, SnapshotView, etc.)
 * - Create assertion-friendly, immutable views from Context instances
 * - API / Web / Mobile modules register via their Module class
 *
 */
public final class ContextViewFactory {
    /* ============================================================
     * DEFAULT VIEW RESOLUTION.
     * ============================================================ */

    /**
     * Internal registry for default view resolution (e.g. DefaultApiContext -> DefaultApiResponseView).
     *
     * Key   : Context class
     * Value : Function to create the default ContextView
     *
     */
    private static final Map<Class<?>, Function<Object, ? extends ContextView>> VIEW_REGISTRY = new ConcurrentHashMap<>();

    // Private constructor to prevent instantiation
    private ContextViewFactory() {}

    /**
     * Registers a context class with its corresponding view factory.
     *
     * @param contextType  the class of the context
     * @param viewFactory  the factory function that creates a ContextView instance from the given context type
     * @throws ContextException if the context type or view factory function is null
     */
    public static <V extends ContextView> void register(Class<?> contextType, Function<Object, V> viewFactory) {
        // Validate inputs
        contextType = validate(contextType, "Context type must not be null");
        viewFactory = validate(viewFactory, "View factory function must not be null");

        // Store the mapping in the registry
        VIEW_REGISTRY.put(contextType, viewFactory);
    }

    /**
     * Creates a ContextView instance from a context instance.
     *
     * @param context    the context instance to create a view from
     * @return the created ContextView instance
     * @throws ContextException if no view is registered for this context type
     */
    @SuppressWarnings("unchecked")
    public static <V extends ContextView> V createView(Object context) {
        // Context instance must not be null
        context = validate(context, "Context instance must not be null");

        // Lookup factory function based on context class
        Function<Object, ? extends ContextView> func = VIEW_REGISTRY.get(context.getClass());

        // If no creator found, throw exception
        func = validate(func, "No view registered for context: " + context.getClass().getName());

        //Apply factory function to context instance to create view
        return (V) func.apply(context);
    }

    /* ============================================================
     * EXPLICIT VIEW CONTRACT RESOLUTION.
     * ============================================================ */

    /**
     * Internal registry for explicit view contract resolution (e.g. DefaultApiContext + RawJsonView -> DefaultRawJsonView).
     *
     * Key   : Context class
     * Value : Function (context, viewContract) -> ContextView
     *
     */
     private static final Map<Class<?>, BiFunction<Object, Class<? extends ContextView> , ? extends ContextView>>
        VIEW_CONTRACT_REGISTRY = new ConcurrentHashMap<>();

    /**
     * Register resolver for specialized view contracts.
     *
     * @param contextType     the concrete Context implementation class
     * @param resolver        the resolver that selects concrete view by view contract
     * @throws ContextException if the context type or view factory function is null
     */
    public static void register(Class<?> contextType, BiFunction<Object, Class<? extends ContextView>, ? extends ContextView> resolver) {
        // Verify context type
        contextType = validate(contextType, "Context type must not be null");

        // Verify resolver function must be provided
        resolver = validate(resolver, "View contract resolver must not be null");

        // Store resolver for this Context type
        VIEW_CONTRACT_REGISTRY.put(contextType, resolver);
    }

    /**
     * Create a specialized ContextView for a given Context instance (e.g. createView(context, RawJsonView.class)).
     *
     * @param context    the context instance
     * @param viewType   the requested view contract type
     * @return concrete ContextView implementation
     */
    @SuppressWarnings("unchecked")
    public static <V extends ContextView> V createView(Object context, Class<V> viewType) {
        // Context instance must not be null
        context = validate(context, "Context instance must not be null");

        // View contract type must be specified
        viewType= validate(viewType, "View type must not be null");

        // Lookup resolver function based on context class and view type
        BiFunction<Object, Class<? extends ContextView> ,? extends ContextView> resolver = VIEW_CONTRACT_REGISTRY.get(context.getClass());

        // Fail fast if no resolver is registered
        resolver = validate(resolver, "No view contract registered for context: " + context.getClass().getName());

        // Resolve and return concrete ContextView
        return (V) resolver.apply(context, viewType);
    }

    /**
     * Clear all registered view mappings (for testing purposes)
     * For test teardown only.
     *
     * ⚠ Framework-internal use only.
     * Intended for test lifecycle cleanup.
     */
    public static void clear() {
        VIEW_REGISTRY.clear();
        VIEW_CONTRACT_REGISTRY.clear();
    }

    /**
     * Requires a non-null value with the given error message.
     *
     * Throws ContextException if the value is null.
     *
     * @param value the value to require
     * @param errorMessage the error message to use if the value is null
     * @return the value if not null
     * @throws ContextException if the value is null
     */
    private static<T> T validate(T value, String message) {
        // Fail fast if missing
        if (value == null) {
            throw new ContextException(message);
        }

        // Return the value
        return value;
    }
}