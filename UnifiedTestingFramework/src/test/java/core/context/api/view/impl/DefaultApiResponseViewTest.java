package core.context.api.view.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import core.context.ContextException;
import core.context.adapter.ResponseAdapter;
import core.context.api.ApiContext;
import core.context.api.ApiContextBuilder;
import core.context.api.Phase5AllTestsSuite;
import core.context.api.adapter.OkHttpAdapter;
import core.context.registry.ContextViewFactory;
import core.utils.LogUtil;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

@ExtendWith(Phase5AllTestsSuite.class) // Use Phase5AllTestsSuite to initialize TestContext
public class DefaultApiResponseViewTest {
    /**
     * Sets up the test class for DefaultApiResponseViewTest.
     *
     * Logs a message indicating the start of the test class.
     */
    @BeforeClass
    public void setUp() {
        LogUtil.info("Starting Phase 5 - Test for DefaultApiResponseView");
    }

    /**
     * Cleans up the test class for DefaultApiResponseViewTest.
     *
     * Logs a message indicating the end of the test class.
     */
    @AfterClass
    public void tearDown() {
        LogUtil.info("Ending Phase 5 - Test for DefaultApiResponseView");
    }

    /**
     * Tests that DefaultApiResponseView fails to build when ResponseAdapter is missing.
     *
     * DefaultApiResponseView should fail to build because context expected to be of type DefaultApiContext.
     */
    @Test
    void constructor_shouldFail_whenResponseAdapterMissing() {
    // OkHttp Request
        Request request = new Request.Builder()
                .url("https://example.com/test")
                .build();

        // OkHttp Response Body
        ResponseBody body = ResponseBody.create(
            "{\"success\":true}",
            MediaType.parse("application/json"));

        // HTTP Client (OkHttp) → raw tool-specific response
        Response response = new Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(201)
                .message("Created")
                .body(body)
                .addHeader("X-Source", "okhttp")
                .build();

        // Raw Tool Adapter (OkHttpAdapter) → ApiContext-neutral ApiResponseAdapter
        ResponseAdapter adapter = OkHttpAdapter.adapt(response);

        // Create ApiContextBuilder: assemble ApiContext from normalized response
        ApiContextBuilder builder = new ApiContextBuilder();

        // Convert ApiContextBuilder into Object to test, missing build method
        Object apiObject = builder.withResponseAdapter(adapter);

        // DefaultApiResponseView should fail to build because context expected to be of type DefaultApiContext
        Exception ex = assertThrows(ContextException.class, () -> new DefaultApiResponseView(apiObject));

        // Verify exception message
        assertTrue(ex.getMessage().contains("Context is expected to be of type DefaultApiContext"));
    }

    /**
     * Tests that DefaultApiResponseView succeeds when ResponseAdapter is provided.
     *
     * Verifies all the expected properties of the created DefaultApiResponseView.
     */
     @Test
    void constructor_shoulSucceed_whenResponseAdapterProvide() {
        // OkHttp Request
        Request request = new Request.Builder()
                .url("https://example.com/test")
                .build();

        // OkHttp Response Body
        ResponseBody body = ResponseBody.create(
            "{\"success\":true}",
            MediaType.parse("application/json"));

        // HTTP Client (OkHttp) → raw tool-specific response
        Response response = new Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(201)
                .message("Created")
                .body(body)
                .addHeader("X-Source", "okhttp")
                .build();

        // Raw Tool Adapter (OkHttpAdapter) → ApiContext-neutral ApiResponseAdapter
        ResponseAdapter adapter = OkHttpAdapter.adapt(response);

        // ApiContextBuilder assemble ApiContext from normalized response
        ApiContextBuilder builder = new ApiContextBuilder();

        // DefaultApiContext → hold API execution state & normalized response
        ApiContext apiContext = builder.withResponseAdapter(adapter).build();

        // Verify ApiContext is not null after builder added ResponseAdapter to ApiContext
        assertNotNull(apiContext, "ApiContext is not null after builder added ResponseAdapter to ApiContext");

        // ContextViewFactory / ApiContextModule → ApiContext → assertion-friendly ApiResponseView
        DefaultApiResponseView view = ContextViewFactory.createView(apiContext);

        // Verify details of ApiResponseView
        assertEquals(201, view.statusCode());
        assertEquals("{\"success\":true}", view.body());

        // Verify ResponseAdapter details exposed by ApiResponseView
        assertEquals(adapter.statusCode(), view.statusCode());
        assertEquals(adapter.body(), view.body());
        assertEquals("okhttp", adapter.headers().get("x-source"));
        assertTrue(view.isSuccess());
    }
}
