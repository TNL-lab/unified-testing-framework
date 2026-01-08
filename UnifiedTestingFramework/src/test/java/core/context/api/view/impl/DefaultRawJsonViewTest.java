package core.context.api.view.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import core.context.TestContext;
import core.context.api.ApiContext;
import core.context.api.ApiContextBuilder;
import core.context.api.Phase5AllTestsSuite;
import core.context.api.adapter.ApiResponseAdapter;
import core.context.api.adapter.OkHttpAdapter;
import core.context.api.view.RawJsonView;
import core.context.registry.ContextViewFactory;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;

@ExtendWith(Phase5AllTestsSuite.class) // Use Phase5AllTestsSuite to initialize TestContext
public class DefaultRawJsonViewTest {
  /**
   * Tests that RawJsonView returns the raw JSON response.
   *
   * Verifies all the expected properties of the created RawJsonView.
   */
  @Test
  public void json_shouldReturnRawJson() {
    // OkHttp Response Body
    ResponseBody body = ResponseBody.create("{\"success\":true}", MediaType.parse("application/json"));

    Headers headers = new Headers.Builder().add("X-Source", "okhttp").build();

    // HTTP Client (OkHttp) → raw tool-specific response
    Response response = Phase5AllTestsSuite.buildOkHttpResponse("https://example.com/test", 201, headers, body);

    // Raw Tool Adapter (OkHttpAdapter) → ApiContext-neutral ApiResponseAdapter
    ApiResponseAdapter adapter = OkHttpAdapter.adapt(response);

    // Verify raw response is same as raw tool-specific response
    assertEquals(
        response,
        adapter.raw(),
        "ApiResponseAdapter raw response should be same as raw tool-specific response");

    // Build ApiContext
    ApiContext apiContext = new ApiContextBuilder().withResponseAdapter(adapter).build();

    // TestContext to store ApiContext for current test lifecycle
    TestContext testContext = Phase5AllTestsSuite.getContextStatic();
    testContext.put(ApiContext.class, apiContext);

    // Get context from TestContext
    ApiContext storedContext = testContext.get(ApiContext.class);

    // Resolve RawJsonView
    RawJsonView rawJsonView = ContextViewFactory.createView(apiContext, RawJsonView.class);

    // Validate
    assertNotNull(rawJsonView, "RawJsonView should not be null");
    assertNotNull(rawJsonView.json(), "RawJsonView json should not be null");

    assertEquals(201, rawJsonView.statusCode(), "Status Code should be 201");
    assertEquals("{\"success\":true}", rawJsonView.body(), "Body should contain expected value");
    assertTrue(rawJsonView.isSuccess());

    assertEquals(adapter.body(), rawJsonView.body());
  }
}
