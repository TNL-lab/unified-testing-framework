package core.context.api.view.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import core.context.TestContext;
import core.context.api.ApiContext;
import core.context.api.ApiContextBuilder;
import core.context.api.Phase5AllTestsSuite;
import core.context.api.adapter.ApiResponseAdapter;
import core.context.api.adapter.OkHttpAdapter;
import core.context.api.view.SnapshotView;
import core.context.registry.ContextViewFactory;
import core.utils.LogUtil;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

@ExtendWith(Phase5AllTestsSuite.class)
public class DefaultSnapshotViewTest {
    /**
     * Sets up the test class for DefaultSnapshotViewTest.
     *
     * Logs a message indicating the start of the test class.
     */
    @BeforeClass
    public void setUp() {
        LogUtil.info("Starting Phase 5 - Test for DefaultSnapshotView");
    }

    /**
     * Cleans up the test class for DefaultSnapshotViewTest.
     *
     * Logs a message indicating the end of the test class.
     */
    @AfterClass
    public void tearDown() {
        LogUtil.info("Ending Phase 5 - Test for DefaultSnapshotView");
    }

    @Test
    public void snapshot_shouldReturnSnapshot() {
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
                .code(204)
                .message("No Content")
                .body(body)
                .addHeader("X-Source", "okhttp")
                .build();

        // Raw Tool Adapter (OkHttpAdapter) → ApiContext-neutral ApiResponseAdapter
        ApiResponseAdapter adapter = OkHttpAdapter.adapt(response);

        // Verify raw response is same as raw tool-specific response
        assertEquals(response, adapter.raw(), "ApiResponseAdapter raw response should be same as raw tool-specific response");

        // Create ApiContextBuilder assemble ApiContext from normalized response
        ApiContextBuilder builder = new ApiContextBuilder();

        // DefaultApiContext to hold API execution state & normalized response
        ApiContext context = builder.withResponseAdapter(adapter).build();

        // TestContext to store ApiContext for current test lifecycle
        TestContext testContext = Phase5AllTestsSuite.getContextStatic();
        testContext.put(ApiContext.class, context);

        // ContextViewFactory / ApiContextModule to ApiContext to assertion-friendly SnapshotView
        SnapshotView view = ContextViewFactory.createView(context, SnapshotView.class);

        // Test Layer to assert only on view (tool & context agnostic)
        //  Verify SnapshotView is not null
        assertNotNull(view, " SnapshotView is null, no snapshot available");

        // Verify Snapshot is not null and matches expected snapshot
        assertNotNull(view.snapshot(), " Snapshot should not be null");
        String expectedSnapshot = "Status: " + view.statusCode() + ", Body: " + view.body();
        assertEquals(expectedSnapshot, view.snapshot(), " Snapshot does not match expected snapshot" );

        // Verify extended fields from ApiResponseView
        assertEquals(204, view.statusCode(), "Status Code should be 204");
        assertEquals("{\"success\":true}", view.body(), "Body should contain expected value");
        assertTrue(view.isSuccess());

        // Verify SnapshotView fields vs ApiResponseAdapter
        assertEquals(adapter.body(), view.body());
        assertEquals("okhttp", adapter.headers().get("x-source"));
    }
}
