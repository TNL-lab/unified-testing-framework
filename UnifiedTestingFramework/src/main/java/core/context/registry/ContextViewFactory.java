package core.context.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import core.context.ContextException;
import core.context.view.ContextView;

/**
 * Factory to resolve ContextView by Context type.
 *
 * Responsibilities:
 * - Register context type -> view converter function
 * - Create ContextView instances
 *
 */
public final class ContextViewFactory {
    // Internal registry mapping a context class to its view supplier.
    private static final Map<Class<?>, Supplier<? extends ContextView>> VIEW_REGISTRY = new ConcurrentHashMap<>();

    // Private constructor to prevent instantiation
    private ContextViewFactory() {}

    /**
     * Registers a context type with its corresponding view supplier.
     *
     * @param contextType the context type to register
     * @param viewSupplier the supplier function to create ContextView instances
     */
    public static void register(Class<?> contextType, Supplier<? extends ContextView> viewSupplier) {
        // Validate inputs
        if (contextType == null) {
            throw new ContextException("Context type must not be null");
        }

        if (viewSupplier == null) {
            throw new ContextException("Creator function must not be null");
        }

        // Store the mapping in the registry
        VIEW_REGISTRY.put(contextType, viewSupplier);
    }


    /**
     * Creates a ContextView instance for the given context type.
     *
     * @param contextType the context type for which to create the view
     * @return a new ContextView instance
     * @throws ContextException if no view is registered
     */
    public static ContextView createView(Class<?> contextType) {
        // Lookup creator function from registry
        Supplier<? extends ContextView> supplier = VIEW_REGISTRY.get(contextType);

        // If no creator found, throw exception
        if (supplier == null) {
            throw new ContextException("No view registered for context type: " + contextType.getName());
        }

      // Create and return the ContextView
        return supplier.get();
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