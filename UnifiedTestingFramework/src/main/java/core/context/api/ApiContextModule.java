package core.context.api;

import java.util.Map;
import java.util.function.Function;

import core.context.ContextException;
import core.context.ContextNamespace;
import core.context.api.view.RawJsonView;
import core.context.api.view.SnapshotView;
import core.context.api.view.impl.DefaultApiResponseView;
import core.context.api.view.impl.DefaultRawJsonView;
import core.context.api.view.impl.DefaultSnapshotView;
import core.context.registry.ContextRegistry;
import core.context.registry.ContextViewFactory;
import core.context.view.ContextView;

/**
 * Module responsible for wiring API contexts and views into the core framework (Registry + ViewFactory).
 *
 * Responsibilities:
 * - Register API context types in ContextRegistry under API namespace
 * - Register API response views in ContextViewFactory (Default implementations)
 * - Centralized wiring point for all API components (API Context & View layer)
 *
 */
public final class ApiContextModule {
    //Constructor to prevent instantiation
    private ApiContextModule() {}

    /**
     * Register API context in ContextRegistry under API namespace
     *
     * This method is used to register both the ApiContext interface and its default implementation (DefaultApiContext)
     */
    private static void registerContexts() {
        // Register ApiContext under API namespace
        ContextRegistry.register(
            ApiContext.class,
            ContextNamespace.API);

        // Register DefaultApiContext under API namespace
        ContextRegistry.register(
            DefaultApiContext.class,
            ContextNamespace.API);
    }

    /**
     * Register API response views in ContextViewFactory
     *
     * This method is used to register the default view implementations for API contexts.
     * It is intended to be called during test setup or framework bootstrap.
     */
    private static void registerViews() {
        // Register default view(DefaultApiResponseView) for DefaultApiContext.
        ContextViewFactory.register(
            DefaultApiContext.class,
            DefaultApiResponseView::new);

        //Register specialized API views for DefaultApiContext
        registerSpecializedViews();
    }

    /**
     * Register specialized API views for DefaultApiContext in ContextViewFactory
     *
     * Responsibilities:
     * - Map view contracts (RawJsonView, SnapshotView) to their default implementations
     * - Centralize all specialized view wiring in a single place
     *
     */
    private static void registerSpecializedViews() {
        // Map specialized view contracts (RawJsonView, SnapshotView) to their default implementations
        Map<Class<? extends ContextView>,
            Function<DefaultApiContext,  ? extends ContextView>> mapping =
            Map.of(
                RawJsonView.class, DefaultRawJsonView::new,
                SnapshotView.class, DefaultSnapshotView::new
            );

        // Register specialized API views(RawJsonView, SnapshotView) for DefaultApiContext
        ContextViewFactory.register(
            DefaultApiContext.class,
            (ctx, viewType) -> {
                // Get the specialized API view factory from the mapping
                Function<DefaultApiContext, ? extends ContextView> factory = mapping.get(viewType);

                // Fast fail if view type is not supported
                if (factory == null) {
                    throw new ContextException("Unsupported specialized API view type: " + viewType.getName());
                }

                // Apply the specialized API view factory
                return factory.apply((DefaultApiContext) ctx);
        });
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
