package core.context.api;

import core.context.ContextException;
import core.context.ContextNamespace;
import core.context.api.view.ApiResponseView;
import core.context.registry.ContextRegistry;
import core.context.registry.ContextViewFactory;

/**
 * Module responsible for wiring API contexts and views into the core framework (Registry + ViewFactory).
 *
 * Responsibilities:
 * - Register API context types into ContextRegistry
 * - Register API response views into ContextViewFactory
 * - Central place to wire all API components
 */
public final class ApiContextModule {
    //Constructor to prevent instantiation
    private ApiContextModule() {}

    /**
     * Register all API context types & views.
     */
    public static void registerContextsAndViews() {
        // Register API context in registry
        ContextRegistry.register(DefaultApiContext.class, ContextNamespace.API);

        // Register API views in view factory
        ContextViewFactory.register(ApiResponseView.class,
            // Supplier get view from current context, wiring only define how to resolve view
            () -> {
                // Only allow ApiResponseView to be created from ApiContext
                throw new ContextException("ApiResponseView must be created from ApiContext via ContextViewFactory");
            });
    }
}
