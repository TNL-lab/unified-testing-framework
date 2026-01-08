package core.context.api.wiring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import core.context.ContextException;
import core.context.ContextNamespace;
import core.context.TestContext;
import core.context.api.ApiContext;
import core.context.api.ApiContextBuilder;
import core.context.api.DefaultApiContext;
import core.context.api.Phase5AllTestsSuite;
import core.context.api.adapter.ApiResponseAdapter;
import core.context.api.adapter.RestAssuredAdapter;
import core.context.api.view.ApiResponseView;
import core.context.api.view.RawJsonView;
import core.context.api.view.impl.DefaultApiResponseView;
import core.context.api.view.impl.DefaultRawJsonView;
import core.context.registry.ContextRegistry;
import core.context.registry.ContextViewFactory;
import core.context.view.ContextView;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;

// This test class verifies the integration of Phase 5 wiring with other phases
@ExtendWith(Phase5AllTestsSuite.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ContextViewFactoryTest {
    /*
     * ============================================================
     * DEFAULT VIEW.
     * ============================================================
     */

    @Test()
    @Order(1)
    public void registerDefaultView_shoudFail_whenContextTypeOrViewMissing() {
        // Attempt to register ContextView with contextType is missing
        Exception exception = assertThrows(
                ContextException.class,
                () -> ContextViewFactory.register(
                        null, ctx -> new DefaultApiResponseView((ApiContext) ctx)),
                " Expected ContextException about contextType to be thrown");

        // Verify the contextType message
        assertSame(
                "Context type must not be null",
                exception.getMessage(),
                " Expected ContextException message to be 'Context type must not be null'");
        assertTrue(
                exception.getMessage().contains("must not be null"),
                " Expected ContextException message to contain 'must not be null'");

        // Attempt to register ContextView with viewFactory is missing
        Function<Object, DefaultApiResponseView> viewFactory = null;
        exception = assertThrows(
                ContextException.class,
                () -> ContextViewFactory.register(DefaultApiContext.class, viewFactory),
                " Expected ContextException about viewFactory to be thrown");

        // Verify the exception message releated to viewFactory
        assertSame(
                "View factory function must not be null",
                exception.getMessage(),
                " Expected ContextException message to be 'View factory function must not be null'");
        assertTrue(
                exception.getMessage().contains("must not be null"),
                " Expected ContextException message to contain 'must not be null'");
    }

    @Test
    @Order(2)
    public void createDefaultView_shoudFail_whenContextMissingOrUnregistered() {
        // Attempt to create ContextView with context instance is missing
        Exception exception = assertThrows(
                ContextException.class,
                () -> ContextViewFactory.createView(null),
                " Expected ContextException about context instance to be thrown");

        // Verify the contextType message
        assertSame(
                "Context instance must not be null",
                exception.getMessage(),
                " Expected ContextException message to be 'Context instance must not be null'");
        assertTrue(
                exception.getMessage().contains("must not be null"),
                " Expected ContextException message to contain 'must not be null'");

        // Attempt to create ContextView with unregistered context instance
        exception = assertThrows(
                ContextException.class,
                () -> ContextViewFactory.createView(String.class),
                " Expected ContextException about context instance to be thrown");

        // Verify the contextType message
        assertTrue(
                exception.getMessage().contains("No view registered for context"),
                " Expected ContextException message to contain 'No view registered for context'");
    }

    @Test
    @Order(3)
    public void registerAndCreateDefaultView_shouldPass_whenContextTypeAndViewFactoryProvided() {
        Headers headers = new io.restassured.http.Headers(
                new Header("Content-Type", "application/json"), new Header("X-Test", "true"));

        // HTTP Client (RestAssured) → raw tool-specific response
        Response response = Phase5AllTestsSuite.buildRestAssuredResponse(200, "{\"id\":1,\"name\":\"John\"}", headers);

        // Raw Tool Adapter (RestAssuredAdapter) → ApiContext-neutral ApiResponseAdapter
        ApiResponseAdapter adapter = RestAssuredAdapter.adapt(response);

        // ApiContextBuilder → assemble ApiContext from normalized response
        ApiContextBuilder builder = new ApiContextBuilder();

        // DefaultApiContext → hold API execution state & normalized response
        ApiContext context = builder.withResponseAdapter(adapter).build();

        // TestContext → store ApiContext for current test lifecycle
        TestContext testContext = new TestContext();

        // Initialize TestContext
        // Register ApiContext under API namespace
        ContextRegistry.register(ApiContext.class, ContextNamespace.API);

        // Register DefaultApiContext under API namespace
        ContextRegistry.register(DefaultApiContext.class, ContextNamespace.API);

        // Register default view for DefaultApiContext
        ContextViewFactory.register(
                DefaultApiContext.class, ctx -> new DefaultApiResponseView((ApiContext) ctx));

        // Store ApiContext in TestContext
        testContext.put(ApiContext.class, context);

        // ContextViewFactory / ApiContextModule → ApiContext → assertion-friendly
        // ApiResponseView
        ApiResponseView view = ContextViewFactory.createView(context);

        // Test Layer → assert only on view (tool & context agnostic)
        assertEquals(DefaultApiResponseView.class, view.getClass());
    }

    /*
     * ============================================================
     * DEFAULT VIEW.
     * ============================================================
     */

    /*
     * ============================================================
     * SPECIALIZED VIEW.
     * ============================================================
     */

    @Test
    @Order(4)
    public void registerSpecializedViews_shoudFail_whenContextTypeOrViewMissing() {
        BiFunction<Object, Class<? extends ContextView>, ContextView> resolver = null;
        // Attempt to register ContextView with viewFactory is missing
        Exception exception = assertThrows(
                ContextException.class,
                () -> ContextViewFactory.register(DefaultApiContext.class, resolver),
                " Expected ContextException about view contract resolver to be thrown");

        // Verify the exception message
        assertSame(
                "View contract resolver must not be null",
                exception.getMessage(),
                " Expected ContextException message to be 'View contract resolver must not be null'");
        assertTrue(
                exception.getMessage().contains("must not be null"),
                " Expected ContextException message to contain 'must not be null'");

        // Attempt to register ContextView with contextType is missing
        BiFunction<Object, Class<? extends ContextView>, ContextView> resolver1 = (ctx, viewClass) -> null;
        exception = assertThrows(
                ContextException.class,
                () -> ContextViewFactory.register(null, resolver1),
                " Expected ContextException about contextType to be thrown");

        // Verify the contextType message
        assertSame(
                "Context type must not be null",
                exception.getMessage(),
                " Expected ContextException message to be 'Context type must not be null'");
        assertTrue(
                exception.getMessage().contains("must not be null"),
                " Expected ContextException message to contain 'must not be null'");
    }

    @Test
    @Order(5)
    public void createSpecializedView_shoudFail_whenContextOrViewMissingOrUnregistered() {
        // Attempt to create ContextView with context instance is missing
        Exception exception = assertThrows(
                ContextException.class,
                () -> ContextViewFactory.createView(null, RawJsonView.class),
                " Expected ContextException about context instance to be thrown");

        // Verify the contextType message
        assertSame(
                "Context instance must not be null",
                exception.getMessage(),
                " Expected ContextException message to be 'Context instance must not be null'");
        assertTrue(
                exception.getMessage().contains("must not be null"),
                " Expected ContextException message to contain 'must not be null'");

        // Attempt to create ContextView with view type is missing
        exception = assertThrows(
                ContextException.class,
                () -> ContextViewFactory.createView(String.class, null),
                " Expected ContextException about view type to be thrown");

        // Verify the contextType message
        assertSame(
                "View type must not be null",
                exception.getMessage(),
                " Expected ContextException message to be 'View type must not be null'");
        assertTrue(
                exception.getMessage().contains("must not be null"),
                " Expected ContextException message to contain 'must not be null'");

        // Attempt to create ContextView with unregistered context instance
        exception = assertThrows(
                ContextException.class,
                () -> ContextViewFactory.createView(String.class, RawJsonView.class),
                " Expected ContextException about context instance to be thrown");

        // Verify the unregistered context instance
        assertTrue(
                exception.getMessage().contains("No view contract registered for context"),
                " Expected ContextException message to contain 'No view contract registered for context'");
    }

    @Test
    @Order(6)
    public void registerAndCreateSpecializedView_shouldPass_whenContextTypeAndViewFactoryProvided() {
        Headers headers = new io.restassured.http.Headers(
                new Header("Content-Type", "application/json"), new Header("X-Test", "true"));

        // HTTP Client (RestAssured) → raw tool-specific response
        Response response = Phase5AllTestsSuite.buildRestAssuredResponse(200, "{\"id\":1,\"name\":\"John\"}", headers);

        // Raw Tool Adapter (RestAssuredAdapter) → ApiContext-neutral ApiResponseAdapter
        ApiResponseAdapter adapter = RestAssuredAdapter.adapt(response);

        // ApiContextBuilder → assemble ApiContext from normalized response
        ApiContextBuilder builder = new ApiContextBuilder();

        // DefaultApiContext → hold API execution state & normalized response
        ApiContext context = builder.withResponseAdapter(adapter).build();

        // TestContext → store ApiContext for current test lifecycle
        TestContext testContext = new TestContext();

        // Initialize TestContext
        // Register ApiContext under API namespace
        ContextRegistry.register(ApiContext.class, ContextNamespace.API);

        // Register DefaultApiContext under API namespace
        ContextRegistry.register(DefaultApiContext.class, ContextNamespace.API);

        // Register specialized API views for DefaultApiContext
        ContextViewFactory.register(
                DefaultApiContext.class,
                (ctx, viewType) -> {
                    return new DefaultRawJsonView((DefaultApiContext) ctx);
                });

        // Store ApiContext in TestContext
        testContext.put(ApiContext.class, context);

        // ContextViewFactory / ApiContextModule → ApiContext → assertion-friendly
        // ApiResponseView
        RawJsonView view = ContextViewFactory.createView(context, RawJsonView.class);

        // Test Layer → assert only on view (tool & context agnostic)
        assertEquals(DefaultRawJsonView.class, view.getClass());
    }

    /*
     * ============================================================
     * SPECIALIZED VIEW.
     * ============================================================
     */
}
