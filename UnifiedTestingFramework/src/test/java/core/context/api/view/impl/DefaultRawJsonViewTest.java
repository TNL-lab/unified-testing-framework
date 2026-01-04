package core.context.api.view.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import core.context.api.ApiContext;
import core.context.api.ApiContextBuilder;
import core.context.api.Phase5AllTestsSuite;
import core.context.api.adapter.ApiResponseAdapter;
import core.context.api.adapter.OkHttpAdapter;
import core.context.api.view.RawJsonView;
import core.context.registry.ContextViewFactory;
import core.utils.LogUtil;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

@ExtendWith(Phase5AllTestsSuite.class) // Use Phase5AllTestsSuite to initialize TestContext
public class DefaultRawJsonViewTest {
    /**
     * Sets up the test class for DefaultRawJsonViewTest.
     *
     * Logs a message indicating the start of the test class.
     */
    @BeforeClass
    public void setUp() {
        LogUtil.info("Starting Phase 5 - Test for DefaultRawJsonView");
    }

    /**
     * Cleans up the test class for DefaultRawJsonViewTest.
     *
     * Logs a message indicating the end of the test class.
     */
    @AfterClass
    public void tearDown() {
        LogUtil.info("Ending Phase 5 - Test for DefaultRawJsonView");
    }

    /**
     * Tests that RawJsonView returns the raw JSON response.
     *
     * Verifies all the expected properties of the created RawJsonView.
     */
    @Test
    public void json_shouldReturnRawJson() {
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
        ApiResponseAdapter adapter = OkHttpAdapter.adapt(response);

        // Verify raw response is same as raw tool-specific response
        assertEquals(response, adapter.raw(), "ApiResponseAdapter raw response should be same as raw tool-specific response");

        // ApiContextBuilder → assemble ApiContext from normalized response
        ApiContextBuilder builder = new ApiContextBuilder();

        // DefaultApiContext → hold API execution state & normalized response
        ApiContext context = builder.withResponseAdapter(adapter).build();

        // ContextViewFactory / ApiContextModule → ApiContext → assertion-friendly RawJsonView
        RawJsonView rawJsonView = ContextViewFactory.createView(context, RawJsonView.class);

        // Test Layer → assert only on view (tool & context agnostic)
        // Verify RawJsonView is not null
        assertNotNull(rawJsonView, "RawJsonView should not be null");

        // Verify RawJsonView json is not null
        assertNotNull(rawJsonView.json(), "RawJsonView json should not be null");

        // Verify extended fields from ApiResponseView
        assertEquals(201, rawJsonView.statusCode(), "Status Code should be 201");
        assertEquals("{\"success\":true}", rawJsonView.body(), "Body should contain expected value");
        assertTrue(rawJsonView.isSuccess());

        // Verify SnapshotView fields vs ApiResponseAdapter
        assertEquals(adapter.body(), rawJsonView.body());
        assertEquals("okhttp", adapter.headers().get("x-source"));
    }
}
