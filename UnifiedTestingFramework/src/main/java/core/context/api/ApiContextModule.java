package core.context.api;

import core.context.ContextNamespace;
import core.context.api.view.impl.DefaultApiResponseView;
import core.context.api.view.impl.DefaultRawJsonView;
import core.context.api.view.impl.DefaultSnapshotView;
import core.context.registry.ContextRegistry;
import core.context.registry.ContextViewFactory;

/**
 * Module responsible for wiring API contexts and views into the core framework (Registry + ViewFactory).
 *
 * Responsibilities:
 * - Register API context types in ContextRegistry under API namespace
 * - Register API response views in ContextViewFactory (Default implementations)
 * - Centralized wiring point for all API components
 *
 */
public final class ApiContextModule {
    //Constructor to prevent instantiation
    private ApiContextModule() {}

    /**
     * Register API context types in ContextRegistry under API namespace
     *
     * This method is used to register both the ApiContext interface and its default implementation (DefaultApiContext)
     */
    private static void registerContexts() {
        // Register ApiContext in ContextRegistry under API namespace
        ContextRegistry.register(ApiContext.class, ContextNamespace.API);

        // Register DefaultApiContext in ContextRegistry under API namespace
        ContextRegistry.register(DefaultApiContext.class, ContextNamespace.API);
    }

    /**
     * Register API response views in ContextViewFactory
     *
     * This method is used to register the default view implementations for API contexts.
     * It is intended to be called during test setup or framework bootstrap.
     */
    private static void registerViews() {
        // Register DefaultApiResponseView factory
        ContextViewFactory.register(DefaultApiContext.class, DefaultApiResponseView::new);

        // Register DefaultRawJsonView factory
        ContextViewFactory.register(DefaultApiContext.class, DefaultRawJsonView::new);

        // Register DefaultSnapshotView factory
        ContextViewFactory.register(DefaultApiContext.class, DefaultSnapshotView::new);
    }

    /**
     * Register all API context types & views.
     *
     * Should be called during test setup or framework bootstrap.
     */
    public static void registerContextsAndViews() {
        // Register API context types in ContextRegistry
        registerContexts();

        // Register API response views in ContextViewFactory
        registerViews();
   }

}
