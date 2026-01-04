package core.context.api.adapter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import core.context.adapter.ResponseAdapter;
import core.context.api.Phase5AllTestsSuite;
import core.utils.LogUtil;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Unit test for OkHttpAdapterTest.
 *
 * Purpose:
 * - Verify OkHttp → ApiResponseAdapter mapping
 * - No HTTP call
 * - No Context / Registry involved
 */
@ExtendWith(Phase5AllTestsSuite.class)
public class OkHttpAdapterTest {
     /**
     * Sets up the test class for OkHttpAdapterTest.
     *
     * Logs a message indicating the start of the test class.
     */
    @BeforeClass
    public void setUp() {
        LogUtil.info("Starting Phase 5 - Test for OkHttpAdapter");
    }

    /**
     * Cleans up the test class for OkHttpAdapterTest.
     *
     * Logs a message indicating the end of the test class.
     */
    @AfterClass
    public void tearDown() {
        LogUtil.info("Ending Phase 5 - Test for OkHttpAdapter");
    }

    @Test
    void adapt_shouldSuccess_whenRestOkHttpProvided() {
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
        ResponseAdapter adapter = OkHttpAdapter.adapt(response);

        // Test Layer → assert only on view (tool & context agnostic)
        // Verify ApiResponseAdapter is not null
        assertNotNull(
            adapter,
            " ApiResponseAdapter should not be null");

        // Verify raw response is same as raw tool-specific response
        assertSame(
            response,
            adapter.raw(),
            "ApiResponseAdapter raw response should be same as raw tool-specific response");

        // Verify fields of ApiResponseAdapter
        assertEquals(
            204,
            adapter.statusCode(),
            " ApiResponseAdapter status code should be 204"
        );
        assertEquals(
            "{\"success\":true}",
            adapter.body(),
            " ApiResponseAdapter body should be same as expected headers"
        );
        assertEquals(
            "okhttp",
            adapter.headers().get("x-source"),
            " ApiResponseAdapter headers should be same as expected headers"
        );
    }
}
