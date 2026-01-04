package core.context.api;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import api.enums.ApiContentType;
import core.context.TestContext;
import core.context.api.adapter.ApiResponseAdapter;
import core.context.api.adapter.OkHttpAdapter;
import core.context.api.adapter.RestAssuredAdapter;
import core.context.api.view.ApiResponseView;
import core.context.api.view.RawJsonView;
import core.context.api.view.SnapshotView;
import core.context.registry.ContextViewFactory;
import core.utils.LogUtil;
import io.restassured.RestAssured;
import io.restassured.builder.ResponseBuilder;
import io.restassured.response.Response;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.ResponseBody;

@ExtendWith(Phase5AllTestsSuite.class) // Use Phase5AllTestsSuite as a JUnit5 extension
public class FullApiFlowTest {
    /**
     * Sets up the test class
     *
     * Logs a message indicating the start of the test class.
     */
    @BeforeClass
    public void setUp() {
        LogUtil.info("Starting Phase 5 - Test for full API context flow with RestAssured");
    }

    /**
     * Cleans up the test class
     *
     * Logs a message indicating the end of the test class.
     */
    @AfterClass
    public void tearDown() {
        LogUtil.info("Ending Phase 5 - Test for full API context flow with RestAssured");
    }

    @Test
    void fullApiFlow_shouldWorkEndToEnd_withRestAssuredAndDefaultView() {
        // HTTP Client (RestAssured) → raw tool-specific response
        Response response = RestAssured.given()
            .baseUri("https://httpbin.org")
            .when()
            .get("/status/200");

        //Raw Tool Adapter (RestAssuredAdapter) → ApiContext-neutral ApiResponseAdapter
        ApiResponseAdapter adapter = RestAssuredAdapter.adapt(response);

        // ApiContextBuilder → assemble ApiContext from normalized response
        ApiContextBuilder builder = new ApiContextBuilder();

        // DefaultApiContext → hold API execution state & normalized response
        ApiContext context = builder.withResponseAdapter(adapter).build();

        // TestContext → store ApiContext for current test lifecycle
        TestContext testContext = Phase5AllTestsSuite.getContextStatic();
        testContext.put(ApiContext.class, context);

        // ContextViewFactory / ApiContextModule → ApiContext → assertion-friendly ApiResponseView
        ApiResponseView view = ContextViewFactory.createView(context);

        // Test Layer → assert only on view (tool & context agnostic)
        assertNotNull(view);
        assertEquals(200, view.statusCode());
        assertTrue(view.isSuccess());
    }

    @Test
    void fullApiFlow_shouldWorkEndToEnd_withRestAssuredAndSpecializedView() {
        // HTTP Client (RestAssured) → raw tool-specific response
        Response response = new ResponseBuilder().setStatusCode(201)
        .setBody("{\"id\":1,\"name\":\"John\"}")
        .setContentType(ApiContentType.JSON.value())
        .setHeader("X-Test", "true")
        .build();

        //Raw Tool Adapter (RestAssuredAdapter) → ApiContext-neutral ApiResponseAdapter
        ApiResponseAdapter adapter = RestAssuredAdapter.adapt(response);

        // ApiContextBuilder → assemble ApiContext from normalized response
        ApiContextBuilder builder = new ApiContextBuilder();

        // DefaultApiContext → hold API execution state & normalized response
        ApiContext context = builder.withResponseAdapter(adapter).build();

        // TestContext → store ApiContext for current test lifecycle
        TestContext testContext = Phase5AllTestsSuite.getContextStatic();
        testContext.put(ApiContext.class, context);

        // ContextViewFactory / ApiContextModule → ApiContext → assertion-friendly ApiResponseView
        RawJsonView view = ContextViewFactory.createView(context, RawJsonView.class);

        // Test Layer → assert only on view (tool & context agnostic)
        // Verify RawJsonView is not null
        assertNotNull(view, "RawJsonView should not be null");

        // Verify RawJsonView json is not null
        assertNotNull(view.json(), "RawJsonView json should not be null");

        // Verify extended fields from ApiResponseView
        assertEquals(
            201,
            view.statusCode(),
            "Status Code should be 201"
        );
        assertEquals(
            "{\"id\":1,\"name\":\"John\"}",
            view.body(),
            "Body should contain expected value"
        );
        assertTrue(view.isSuccess());

        // Verify SnapshotView fields vs ApiResponseAdapter
        assertEquals(adapter.body(), view.body());
    }

    @Test
    void fullApiFlow_shouldWorkEndToEnd_withOkHttpAndDefaultView() {
        // OkHttp Request
        Request request = new Request.Builder()
                .url("https://example.com/test")
                .build();

        // OkHttp Response Body
        ResponseBody body = ResponseBody.create(
            "{\"success\":true}",
            MediaType.parse("application/json"));

        // HTTP Client (OkHttp) → raw tool-specific response
        okhttp3.Response response = new okhttp3.Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(204)
                .message("No Content")
                .body(body)
                .addHeader("X-Source", "okhttp")
                .build();

        //Raw Tool Adapter (OkHttpAdapter) → ApiContext-neutral ApiResponseAdapter
        ApiResponseAdapter adapter = OkHttpAdapter.adapt(response);

        // ApiContextBuilder → assemble ApiContext from normalized response
        ApiContextBuilder builder = new ApiContextBuilder();

        // DefaultApiContext → hold API execution state & normalized response
        ApiContext context = builder.withResponseAdapter(adapter).build();

        // TestContext → store ApiContext for current test lifecycle
        TestContext testContext = Phase5AllTestsSuite.getContextStatic();
        testContext.put(ApiContext.class, context);

        // ContextViewFactory / ApiContextModule → ApiContext → assertion-friendly ApiResponseView
        ApiResponseView view = ContextViewFactory.createView(context);

        // Test Layer → assert only on view (tool & context agnostic)
        //  Verify SnapshotView is not null
        assertNotNull(view, " ApiResponseView must not be null");

        // Verify extended fields from ApiResponseView
        assertEquals(
            204,
            view.statusCode(),
            "Status Code should be 204"
        );
        assertEquals(
            "{\"success\":true}",
            view.body(),
            "Body should contain expected value"
        );
        assertTrue(view.isSuccess());

        // Verify ApiResponseView fields vs ApiResponseAdapter
        assertEquals(
            adapter.body(),
            view.body(),
            " ApiResponseView body should be same as ApiResponseAdapter body"
        );
        assertEquals("okhttp", adapter.headers().get("x-source"));
    }

    @Test
    void fullApiFlow_shouldWorkEndToEnd_withOkHttpAndSpecializedView() {
        // OkHttp Request
        Request request = new Request.Builder()
                .url("https://example.com/test")
                .build();

        // OkHttp Response Body
        ResponseBody body = ResponseBody.create(
            "{\"success\":true}",
            MediaType.parse("application/json"));

        // HTTP Client (OkHttp) → raw tool-specific response
        okhttp3.Response response = new okhttp3.Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(204)
                .message("No Content")
                .body(body)
                .addHeader("X-Source", "okhttp")
                .build();

        //Raw Tool Adapter (RestAssuredAdapter) → ApiContext-neutral ApiResponseAdapter
        ApiResponseAdapter adapter = OkHttpAdapter.adapt(response);

        // ApiContextBuilder → assemble ApiContext from normalized response
        ApiContextBuilder builder = new ApiContextBuilder();

        // DefaultApiContext → hold API execution state & normalized response
        ApiContext context = builder.withResponseAdapter(adapter).build();

        // TestContext → store ApiContext for current test lifecycle
        TestContext testContext = Phase5AllTestsSuite.getContextStatic();
        testContext.put(ApiContext.class, context);

        // ContextViewFactory / ApiContextModule → ApiContext → assertion-friendly specilialized view
        SnapshotView view = ContextViewFactory.createView(context, SnapshotView.class);

        // Test Layer → assert only on view (tool & context agnostic)
        //  Verify SnapshotView is not null
        assertNotNull(
            view,
            " SnapshotView is null, no snapshot available"
        );

        // Verify Snapshot is not null and matches expected snapshot
        assertNotNull(
            view.snapshot(),
            " Snapshot should not be null"
        );
        String expectedSnapshot = "Status: " + view.statusCode() + ", Body: " + view.body();
        assertEquals(
            expectedSnapshot,
            view.snapshot(),
            " Snapshot does not match expected snapshot"
        );

        // Verify extended fields from ApiResponseView
        assertEquals(
            204,
            view.statusCode(),
            "Status Code should be 204");
        assertEquals(
            "{\"success\":true}",
            view.body(),
            "Body should contain expected value");
        assertTrue(view.isSuccess());
    }
}
