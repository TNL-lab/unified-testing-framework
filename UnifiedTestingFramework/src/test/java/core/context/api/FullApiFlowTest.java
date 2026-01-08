package core.context.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import core.context.TestContext;
import core.context.api.adapter.ApiResponseAdapter;
import core.context.api.adapter.OkHttpAdapter;
import core.context.api.adapter.RestAssuredAdapter;
import core.context.api.view.ApiResponseView;
import core.context.api.view.RawJsonView;
import core.context.api.view.SnapshotView;
import core.context.registry.ContextViewFactory;
import io.restassured.http.Header;
import io.restassured.response.Response;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.ResponseBody;

@ExtendWith(Phase5AllTestsSuite.class) // Use Phase5AllTestsSuite as a JUnit5 extension
public class FullApiFlowTest {
  @Test
  void fullApiFlow_shouldWorkEndToEnd_withRestAssuredAndDefaultView() {
    // ==============================================================
    // (1) REQUEST & RESPONSE NORMALIZATION
    // HTTP Client → Tool Adapter → ApiResponseAdapter
    // ==============================================================

    io.restassured.http.Headers headers = new io.restassured.http.Headers(
        new Header("Content-Type", "application/json"), new Header("X-Test", "true"));

    // Build RestAssured Response
    Response response = Phase5AllTestsSuite.buildRestAssuredResponse(200, "{\"id\":1,\"name\":\"John\"}", headers);

    // Tool Adapter (RestAssuredAdapter) → Api-neutral ApiResponseAdapter
    ApiResponseAdapter adapter = RestAssuredAdapter.adapt(response);

    // ==============================================================
    // (2) CONTEXT CREATION & LIFECYCLE
    // Builder → Context → Store in TestContext
    // ==============================================================

    // Build ApiContext
    ApiContext context = new ApiContextBuilder().withResponseAdapter(adapter).build();

    // TestContext → store ApiContext for current test lifecycle
    TestContext testContext = Phase5AllTestsSuite.getContextStatic();
    testContext.put(ApiContext.class, context);

    // ==============================================================
    // (3) VIEW RESOLUTION
    // Context → View Factory → View
    // ==============================================================

    // Get context from TestContext
    ApiContext storedContext = testContext.get(ApiContext.class);

    // Resolve default ApiResponseView
    ApiResponseView view = ContextViewFactory.createView(storedContext);

    // ==============================================================
    // (4) VALIDATION / ASSERTION
    // Test Layer → assert only on view (tool & context agnostic)
    // ==============================================================

    assertNotNull(view);
    assertEquals(200, view.statusCode());
    assertTrue(view.isSuccess());
  }

  @Test
  void fullApiFlow_shouldWorkEndToEnd_withRestAssuredAndSpecializedView() {
    // ==============================================================
    // (1) REQUEST & RESPONSE NORMALIZATION
    // HTTP Client → Tool Adapter → ApiResponseAdapter
    // ==============================================================

    io.restassured.http.Headers headers = new io.restassured.http.Headers(
        new Header("Content-Type", "application/json"), new Header("X-Test", "true"));

    // Build RestAssured Response
    Response response = Phase5AllTestsSuite.buildRestAssuredResponse(201, "{\"id\":1,\"name\":\"John\"}", headers);

    // Raw Tool Adapter (RestAssuredAdapter) → Api-neutral ApiResponseAdapter
    ApiResponseAdapter adapter = RestAssuredAdapter.adapt(response);

    // ==============================================================
    // (2) CONTEXT CREATION & LIFECYCLE
    // Builder → Context → Store in TestContext
    // ==============================================================

    // Build ApiContext
    ApiContext context = new ApiContextBuilder().withResponseAdapter(adapter).build();

    // TestContext → store ApiContext for current test lifecycle
    TestContext testContext = Phase5AllTestsSuite.getContextStatic();
    testContext.put(ApiContext.class, context);

    // ==============================================================
    // (3) VIEW RESOLUTION
    // Context → View Factory → View
    // ==============================================================

    // Get context from TestContext
    ApiContext storedContext = testContext.get(ApiContext.class);

    // Resolve specialized views
    RawJsonView view = ContextViewFactory.createView(storedContext, RawJsonView.class);

    // ==============================================================
    // (4) VALIDATION / ASSERTION
    // Test Layer → assert only on view (tool & context agnostic)
    // ==============================================================
    assertNotNull(view, "RawJsonView should not be null");
    assertNotNull(view.json(), "RawJsonView json should not be null");
    assertEquals(201, view.statusCode(), "Status Code should be 201");
    assertEquals("{\"id\":1,\"name\":\"John\"}", view.body(), "Body should contain expected value");
    assertTrue(view.isSuccess());
  }

  @Test
  void fullApiFlow_shouldWorkEndToEnd_withOkHttpAndDefaultView() {

    // ==============================================================
    // (1) REQUEST & RESPONSE NORMALIZATION
    // HTTP Client → Tool Adapter → ApiResponseAdapter
    // ==============================================================

    Headers headers = new Headers.Builder().add("X-Source", "okhttp").build();

    // OkHttp Response Body
    ResponseBody body = ResponseBody.create("{\"success\":true}", MediaType.parse("application/json"));

    // HTTP Client (OkHttp) → raw tool-specific response
    okhttp3.Response response = Phase5AllTestsSuite.buildOkHttpResponse("https://example.com/test", 204, headers, body);

    // Tool Adapter (OkHttpAdapter) → Api-neutral ApiResponseAdapter
    ApiResponseAdapter adapter = OkHttpAdapter.adapt(response);

    // ==============================================================
    // (2) CONTEXT CREATION & LIFECYCLE
    // Builder → Context → Store in TestContext
    // ==============================================================

    // Build ApiContext
    ApiContext context = new ApiContextBuilder().withResponseAdapter(adapter).build();

    // TestContext → store ApiContext for current test lifecycle
    TestContext testContext = Phase5AllTestsSuite.getContextStatic();
    testContext.put(ApiContext.class, context);

    // ==============================================================
    // (3) VIEW RESOLUTION
    // Context → View Factory → View
    // ==============================================================

    // Get context from TestContext
    ApiContext storedContext = testContext.get(ApiContext.class);

    // Resolve default ApiResponseView
    ApiResponseView view = ContextViewFactory.createView(storedContext);

    // ==============================================================
    // (4) VALIDATION / ASSERTION
    // Test Layer → assert only on view (tool & context agnostic)
    // ==============================================================

    assertNotNull(view, " ApiResponseView must not be null");
    assertEquals(204, view.statusCode(), "Status Code should be 204");
    assertEquals("{\"success\":true}", view.body(), "Body should contain expected value");
    assertTrue(view.isSuccess());
    assertEquals("okhttp", adapter.headers().get("x-source"));
  }

  @Test
  void fullApiFlow_shouldWorkEndToEnd_withOkHttpAndSpecializedView() {

    // ==============================================================
    // (1) REQUEST & RESPONSE NORMALIZATION
    // HTTP Client → Tool Adapter → ApiResponseAdapter
    // ==============================================================

    Headers headers = new Headers.Builder().add("X-Source", "okhttp").build();

    // OkHttp Response Body
    ResponseBody body = ResponseBody.create("{\"success\":true}", MediaType.parse("application/json"));

    // HTTP Client (OkHttp) → raw tool-specific response
    okhttp3.Response response = Phase5AllTestsSuite.buildOkHttpResponse("https://example.com/test", 204, headers, body);

    // Raw Tool Adapter (RestAssuredAdapter) → Api-neutral ApiResponseAdapter
    ApiResponseAdapter adapter = OkHttpAdapter.adapt(response);

    // ==============================================================
    // (2) CONTEXT CREATION & LIFECYCLE
    // Builder → Context → Store in TestContext
    // ==============================================================

    // Build ApiContext
    ApiContext context = new ApiContextBuilder().withResponseAdapter(adapter).build();

    // TestContext → store ApiContext for current test lifecycle
    TestContext testContext = Phase5AllTestsSuite.getContextStatic();
    testContext.put(ApiContext.class, context);

    // ==============================================================
    // (3) VIEW RESOLUTION
    // Context → View Factory → View
    // ==============================================================

    // Get context from TestContext
    ApiContext storedContext = testContext.get(ApiContext.class);

    // Resolve SnapshotView
    SnapshotView view = ContextViewFactory.createView(storedContext, SnapshotView.class);

    // ==============================================================
    // (4) VALIDATION / ASSERTION
    // Test Layer → assert only on view (tool & context agnostic)
    // ==============================================================

    assertNotNull(view, " SnapshotView is null, no snapshot available");
    assertNotNull(view.snapshot(), " Snapshot should not be null");

    String expectedSnapshot = "Status: " + view.statusCode() + ", Body: " + view.body();
    assertEquals(expectedSnapshot, view.snapshot(), " Snapshot does not match expected snapshot");

    assertEquals(204, view.statusCode(), "Status Code should be 204");
    assertEquals("{\"success\":true}", view.body(), "Body should contain expected value");
    assertTrue(view.isSuccess());
  }
}
