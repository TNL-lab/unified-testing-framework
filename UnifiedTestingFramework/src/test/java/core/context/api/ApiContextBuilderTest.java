package core.context.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import core.context.ContextException;
import core.context.TestContext;
import core.context.api.adapter.ApiResponseAdapter;
import core.context.api.adapter.RestAssuredAdapter;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;

/**
 * - Builder fail fast
 * - Context only stores the ResponseAdapter without performing any
 * transformation
 */
@ExtendWith(Phase5AllTestsSuite.class) // Use Phase5AllTestsSuite to initialize TestContext
public class ApiContextBuilderTest {
  /**
   * Test that ApiContextBuilder fails to build when ResponseAdapter is missing.
   *
   * Verify that ContextException is thrown with a message containing
   * "ResponseAdapter".
   */
  @Test
  void build_shouldFail_whenResponseAdapterMissing() {
    // Create ApiContextBuilder: assemble ApiContext from normalized response
    ApiContextBuilder builder = new ApiContextBuilder();

    // ApiContextBuilder should fail to build because ApiResponseAdapter is missing
    ContextException exception = assertThrows(ContextException.class, builder::build);

    // Verify exception message
    assertTrue(exception.getMessage().contains("ApiResponseAdapter"));
  }

  /**
   * Tests that ApiContextBuilder creates an ApiContext when ResponseAdapter is
   * provided.
   *
   * Verifies all the expected properties of the created ApiContext.
   */
  @Test
  void build_shouldCreateApiContext_whenResponseAdapterProvide() {
    Headers headers = new Headers(new Header("Set-Cookie", "A=1"), new Header("Set-Cookie", "B=1"));

    // HTTP Client (RestAssured) → raw tool-specific response
    Response response = Phase5AllTestsSuite.buildRestAssuredResponse(200, "Hello World", headers);

    // Convert RestAssuredResponse into ResponseAdapter
    ApiResponseAdapter adapter = RestAssuredAdapter.adapt(response);

    // Build ApiContext from normalized response
    ApiContext apiContext = new ApiContextBuilder().withResponseAdapter(adapter).build();

    assertNotNull(apiContext, " ApiContext should not be null");
    assertSame(adapter, apiContext.response(), "Adapter should be stored correctly");

    // TestContext → store ApiContext for current test lifecycle
    TestContext testContext = Phase5AllTestsSuite.getContextStatic();
    testContext.put(ApiContext.class, apiContext);

    // Retrieve ApiContext from TestContext
    ApiContext retrieveDefaultApiContext = testContext.get(ApiContext.class);

    assertNotNull(retrieveDefaultApiContext, "Retrieved ApiContext should not be null");
    assertSame(
        adapter,
        retrieveDefaultApiContext.response(),
        "Adapter should match after retrieval of ApiContext from TestContext");

    assertNotNull(apiContext.response().body(), "Body of ResponseAdapter should not be null");
    assertNotNull(apiContext.response().headers(), "Headers of ResponseAdapter should not be null");
    assertEquals(200, apiContext.response().statusCode(), "Status code should be 200");
  }
}
