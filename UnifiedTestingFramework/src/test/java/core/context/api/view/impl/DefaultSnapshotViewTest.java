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
import core.context.api.view.SnapshotView;
import core.context.registry.ContextViewFactory;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;

@ExtendWith(Phase5AllTestsSuite.class)
public class DefaultSnapshotViewTest {
  @Test
  public void snapshot_shouldReturnSnapshot() {
    // OkHttp Response Body
    ResponseBody body = ResponseBody.create("{\"success\":true}", MediaType.parse("application/json"));

    Headers headers = new Headers.Builder().add("X-Source", "okhttp").build();

    // HTTP Client (OkHttp) → raw tool-specific response
    Response response = Phase5AllTestsSuite.buildOkHttpResponse("https://example.com/test", 204, headers, body);

    // Raw Tool Adapter (OkHttpAdapter) → ApiContext-neutral ApiResponseAdapter
    ApiResponseAdapter adapter = OkHttpAdapter.adapt(response);

    // Create ApiContextBuilder assemble ApiContext from normalized response
    ApiContext context = new ApiContextBuilder().withResponseAdapter(adapter).build();

    // TestContext to store ApiContext for current test lifecycle
    TestContext testContext = Phase5AllTestsSuite.getContextStatic();
    testContext.put(ApiContext.class, context);

    // Get context from TestContext
    ApiContext storedContext = testContext.get(ApiContext.class);

    // Resolve SnapshotView
    SnapshotView view = ContextViewFactory.createView(context, SnapshotView.class);

    // Validate
    assertNotNull(view, " SnapshotView is null, no snapshot available");
    assertNotNull(view.snapshot(), " Snapshot should not be null");

    String expectedSnapshot = "Status: " + view.statusCode() + ", Body: " + view.body();
    assertEquals(expectedSnapshot, view.snapshot(), " Snapshot does not match expected snapshot");

    assertEquals(204, view.statusCode(), "Status Code should be 204");
    assertEquals("{\"success\":true}", view.body(), "Body should contain expected value");
    assertTrue(view.isSuccess());
  }
}
