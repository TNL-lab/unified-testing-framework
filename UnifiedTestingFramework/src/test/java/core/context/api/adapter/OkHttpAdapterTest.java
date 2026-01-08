package core.context.api.adapter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import core.context.ContextException;
import core.context.api.Phase5AllTestsSuite;
import okhttp3.Headers;
import okhttp3.MediaType;
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
    @Test
    void adapt_shouldFail_whenReadingBodyFails() throws IOException {
        // Fake response body
        ResponseBody body = Mockito.mock(ResponseBody.class);
        Mockito.when(body.string()).thenThrow(new IOException("Read failed"));

        Headers headers = new Headers.Builder().add("X-Source", "okhttp").build();

        // HTTP Client (OkHttp) → raw tool-specific response
        Response response = Phase5AllTestsSuite.buildOkHttpResponse("https://example.com/test", 204, headers, body);

        // Validation
        Exception exception = assertThrows(ContextException.class, () -> OkHttpAdapter.adapt(response));

        assertEquals("Error reading response body as string", exception.getMessage());

        assertTrue(exception.getMessage().contains("Error reading response"));
    }

    @Test
    void adapt_shouldSuccess_whenReadingBodyIsNull() {
        Headers headers = new Headers.Builder().add("X-Source", "okhttp").build();

        // HTTP Client (OkHttp) → raw tool-specific response
        Response response = Phase5AllTestsSuite.buildOkHttpResponse("https://example.com/test", 204, headers, null);

        // Raw Tool Adapter (OkHttpAdapter) → Api-neutral ApiResponseAdapter
        ApiResponseAdapter adapter = OkHttpAdapter.adapt(response);

        // Validation
        assertNotNull(adapter, " ApiResponseAdapter should not be null");
        assertSame(
                response,
                adapter.raw(),
                "ApiResponseAdapter raw response should be same as raw tool-specific response");
        assertEquals(204, adapter.statusCode(), " ApiResponseAdapter status code should be 204");
        assertNull(adapter.body(), " Response body must be null");
        assertEquals(
                "okhttp",
                adapter.headers().get("x-source"),
                " ApiResponseAdapter headers should be same as expected headers");
    }

    @Test
    void adapt_shouldSuccess_whenOkHttpProvided() {
        Headers headers = new Headers.Builder().add("X-Source", "okhttp").build();

        // OkHttp Response Body
        ResponseBody body = ResponseBody.create("{\"success\":true}", MediaType.parse("application/json"));

        // HTTP Client (OkHttp) → raw tool-specific response
        Response response = Phase5AllTestsSuite.buildOkHttpResponse("https://example.com/test", 204, headers, body);

        // Raw Tool Adapter (OkHttpAdapter) → Api-neutral ApiResponseAdapter
        ApiResponseAdapter adapter = OkHttpAdapter.adapt(response);

        // Test Layer → assert only on view (tool & context agnostic)
        assertNotNull(adapter, " ApiResponseAdapter should not be null");
        assertSame(
                response,
                adapter.raw(),
                "ApiResponseAdapter raw response should be same as raw tool-specific response");
        assertEquals(204, adapter.statusCode(), " ApiResponseAdapter status code should be 204");
        assertEquals(
                "{\"success\":true}",
                adapter.body(),
                " ApiResponseAdapter body should be same as expected headers");
        assertEquals(
                "okhttp",
                adapter.headers().get("x-source"),
                " ApiResponseAdapter headers should be same as expected headers");
    }

    @Test
    void adapt_shouldJoinMultipleHeaderValuesWithComma() {
        // OkHttp Headers
        Headers headers = new Headers.Builder().add("Set-Cookie", "A=1").add("Set-Cookie", "B=2").build();

        // OkHttp Response Body
        ResponseBody body = ResponseBody.create("ok", null);

        // HTTP Client (OkHttp) → raw tool-specific response
        Response response = Phase5AllTestsSuite.buildOkHttpResponse("https://example.com/test", 204, headers, body);

        // Raw Tool Adapter (OkHttpAdapter) → Api-neutral ApiResponseAdapter
        ApiResponseAdapter adapter = OkHttpAdapter.adapt(response);

        // Validate
        assertEquals("A=1,B=2", adapter.headers().get("set-cookie"));
    }
}
