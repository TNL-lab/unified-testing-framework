package core.context.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import core.context.ContextException;
import core.context.api.adapter.ApiResponseAdapter;
import core.context.api.adapter.RestAssuredAdapter;
import io.restassured.http.Headers;
import io.restassured.response.Response;

/** - Context is immutable holder - No hidden logic */
@ExtendWith(Phase5AllTestsSuite.class)
public class DefaultApiContextTest {
  @Test
  void constructor_shouldFail_whenResponseAdapterIsMissing() {
    // Attempt to create DefaultApiContext with ResponseAdapter is missing
    Exception ex = assertThrows(ContextException.class, () -> new DefaultApiContext(null));

    // Verify the exception message
    assertEquals(
        "Api Response Adapter must not be null",
        ex.getMessage(),
        " Expected ContextException message to be 'Response Adapter must not be null'");
    assertTrue(
        ex.getMessage().contains("must not be null"),
        " Expected ContextException message to contain 'must not be null'");
  }

  @Test
  void constructor_shouldSucceed_whenResponseAdapterProvided() {
    Headers headers = new Headers();

    // HTTP Client (RestAssured) → raw tool-specific response
    Response response = Phase5AllTestsSuite.buildRestAssuredResponse(200, "Hello World", headers);

    // Raw Tool Adapter (RestAssuredAdapter) → ApiContext-neutral ApiResponseAdapter
    ApiResponseAdapter adapter = RestAssuredAdapter.adapt(response);

    // Create DefaultApiContext
    DefaultApiContext defaultApiContext = new DefaultApiContext(adapter);

    // Validate
    assertEquals(
        adapter.raw(),
        defaultApiContext.response().raw(),
        " DefaultApiContext response should be same as ApiResponseAdapter raw response");

    // Verify fields of ResponseAdapter
    assertEquals(
        200,
        defaultApiContext.response().statusCode(),
        "DefaultApiContext status code should be 200");
    assertNotNull(
        defaultApiContext.response().headers(), "DefaultApiContext headers should not be null");
    assertNotNull(defaultApiContext.response().body(), "DefaultApiContext body should not be null");
  }
}
