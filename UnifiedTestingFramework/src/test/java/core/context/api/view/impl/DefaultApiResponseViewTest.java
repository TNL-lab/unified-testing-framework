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
import core.context.api.view.ApiResponseView;
import core.context.registry.ContextViewFactory;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;

@ExtendWith(Phase5AllTestsSuite.class) // Use Phase5AllTestsSuite to initialize TestContext
public class DefaultApiResponseViewTest {
  /**
   * Tests that DefaultApiResponseView succeeds when ResponseAdapter is provided.
   *
   * Verifies all the expected properties of the created DefaultApiResponseView.
   */
  @Test
  void constructor_shouldSucceed_whenResponseAdapterProvide() {
    // OkHttp Response Body
    ResponseBody body = ResponseBody.create("{\"success\":true}", MediaType.parse("application/json"));

    Headers headers = new Headers.Builder().add("X-Source", "okhttp").build();

    // HTTP Client (OkHttp) → raw tool-specific response
    Response response = Phase5AllTestsSuite.buildOkHttpResponse("https://example.com/test", 201, headers, body);

    // Raw Tool Adapter (OkHttpAdapter) → ApiContext-neutral ApiResponseAdapter
    ApiResponseAdapter adapter = OkHttpAdapter.adapt(response);

    // Build ApiContext
    ApiContext apiContext = new ApiContextBuilder().withResponseAdapter(adapter).build();

    // TestContext to store ApiContext for current test lifecycle
    TestContext testContext = Phase5AllTestsSuite.getContextStatic();
    testContext.put(ApiContext.class, apiContext);

    // Get context from TestContext
    ApiContext storedContext = testContext.get(ApiContext.class);

    // Resolve ApiResponseView
    ApiResponseView view = ContextViewFactory.createView(apiContext);

    // Validate
    assertNotNull(view, " DefaultApiResponseView is not null");
    assertEquals(201, view.statusCode());
    assertEquals("{\"success\":true}", view.body());
    assertTrue(view.isSuccess());

    assertEquals(adapter.statusCode(), view.statusCode());
    assertEquals(adapter.body(), view.body());
    assertEquals("okhttp", adapter.headers().get("x-source"));
  }
}
