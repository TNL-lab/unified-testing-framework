package core.context.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import core.context.ContextException;
import core.context.view.ContextView;

/**
 * Factory to resolve ContextView by Context type.
 *
 * Responsibilities:
 * - Register context type -> view factory function
 * - Create ContextView instances
 * - API / Web / Mobile modules register via their Module class
 *
 */
public final class ContextViewFactory {
    // Internal registry maps a context class to a factory function that creates its view
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
        if (contextType == null) {
            throw new ContextException("Context type must not be null");
        }

        if (viewFactory == null) {
            throw new ContextException("View factory function must not be null");
        }

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
        if (context == null ) {
             throw new ContextException("Context instance must not be null");
        }

        // Lookup factory function based on context class
        Function<Object, ? extends ContextView> func = VIEW_REGISTRY.get(context.getClass());

        // If no creator found, throw exception
        if (func == null) {
            throw new ContextException("No view registered for context: " + context.getClass());
        }

        //Apply factory function to context instance to create view
        return (V) func.apply(context);
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
    }
}