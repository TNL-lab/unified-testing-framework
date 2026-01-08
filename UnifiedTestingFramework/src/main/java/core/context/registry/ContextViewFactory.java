package core.context.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

import core.context.ContextException;
import core.context.support.ContextPreconditions;
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
    /*
     * ============================================================
     * DEFAULT VIEW RESOLUTION.
     * ============================================================
     */

    /**
     * Internal registry for default view resolution (e.g. DefaultApiContext ->
     * DefaultApiResponseView).
     *
     * Key : Context class
     * Value : Function to create the default ContextView
     *
     */
    private static final Map<Class<?>, Function<Object, ? extends ContextView>> VIEW_REGISTRY = new ConcurrentHashMap<>();

    // Private constructor to prevent instantiation
    private ContextViewFactory() {
    }

    /**
     * Registers a context class with its corresponding view factory.
     *
     * @param contextType the class of the context
     * @param viewFactory the factory function that creates a ContextView instance
     *                    from the given context type
     * @throws ContextException if the context type or view factory function is null
     */
    public static <V extends ContextView> void register(Class<?> contextType, Function<Object, V> viewFactory) {
        // Fail fast if context type is null
        ContextPreconditions.requireNonNull(contextType, "Context type must not be null");

        // Fail fast if view factory function is null
        ContextPreconditions.requireNonNull(viewFactory, "View factory function must not be null");

        // Store the mapping in the registry
        VIEW_REGISTRY.put(contextType, viewFactory);
    }

    /**
     * Creates a ContextView instance from a context instance.
     *
     * @param context the context instance to create a view from
     * @return the created ContextView instance
     * @throws ContextException if no view is registered for this context type
     */
    @SuppressWarnings("unchecked")
    public static <V extends ContextView> V createView(Object context) {
        // Fail fast if context instance is null
        ContextPreconditions.requireNonNull(context, "Context instance must not be null");

        // Lookup factory function based on context class
        Function<Object, ? extends ContextView> func = resolveFactory(context, VIEW_REGISTRY);

        // If no creator found, throw exception
        ContextPreconditions.requireNonNull(func, "No view registered for context: " + context.getClass().getName());

        // Apply factory function to context instance to create view
        return (V) func.apply(context);
    }

    /*
     * ============================================================
     * EXPLICIT VIEW CONTRACT RESOLUTION.
     * ============================================================
     */

    /**
     * Internal registry for explicit view contract resolution (e.g.
     * DefaultApiContext + RawJsonView -> DefaultRawJsonView).
     *
     * Key : Context class
     * Value : Function (context, viewContract) -> ContextView
     *
     */
    private static final Map<Class<?>, BiFunction<Object, Class<? extends ContextView>, ? extends ContextView>> VIEW_CONTRACT_REGISTRY = new ConcurrentHashMap<>();

    /**
     * Register resolver for specialized view contracts.
     *
     * @param contextType the concrete Context implementation class
     * @param resolver    the resolver that selects concrete view by view contract
     * @throws ContextException if the context type or view factory function is null
     */
    public static void register(Class<?> contextType,
            BiFunction<Object, Class<? extends ContextView>, ? extends ContextView> resolver) {
        // Fail fast if context type is null
        ContextPreconditions.requireNonNull(contextType, "Context type must not be null");

        // Fail fast if resolver function is null
        ContextPreconditions.requireNonNull(resolver, "View contract resolver must not be null");

        // Store resolver for this Context type
        VIEW_CONTRACT_REGISTRY.put(contextType, resolver);
    }

    /**
     * Create a specialized ContextView for a given Context instance (e.g.
     * createView(context, RawJsonView.class)).
     *
     * @param context  the context instance
     * @param viewType the requested view contract type
     * @return concrete ContextView implementation
     */
    @SuppressWarnings("unchecked")
    public static <V extends ContextView> V createView(Object context, Class<V> viewType) {
        // Fail fast if context instance is null
        ContextPreconditions.requireNonNull(context, "Context instance must not be null");

        // Fail fast if view type is null
        ContextPreconditions.requireNonNull(viewType, "View type must not be null");

        // Lookup resolver function based on context class and view type
        BiFunction<Object, Class<? extends ContextView>, ? extends ContextView> resolver = resolveFactory(context,
                VIEW_CONTRACT_REGISTRY);

        // Fail fast if no resolver is registered
        ContextPreconditions.requireNonNull(resolver,
                "No view contract registered for context: " + context.getClass().getName());

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

    private static <T> T resolveFactory(Object context, Map<Class<?>, T> registry) {
        Class<?> clazz = context.getClass();

        T factory = registry.get(clazz);
        if (factory != null)
            return factory;

        for (Class<?> iface : clazz.getInterfaces()) {
            factory = registry.get(iface);
            if (factory != null)
                return factory;
        }

        Class<?> superClass = clazz.getSuperclass();
        while (superClass != null && superClass != Object.class) {
            factory = registry.get(superClass);
            if (factory != null)
                return factory;
            superClass = superClass.getSuperclass();
        }

        return null;
    }
}