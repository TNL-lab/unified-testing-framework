package core.context.api.adapter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import core.context.api.Phase5AllTestsSuite;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;

/**
 * Unit test for RestAssuredAdapter.
 *
 * Purpose:
 * - Verify mapping from RestAssured Response
 * - No HTTP call
 * - No Context / Registry involved
 */
@ExtendWith(Phase5AllTestsSuite.class)
public class RestAssuredAdapterTest {
  @Test
  void adapt_shouldSuccess_whenRestAssuredResponseProvided() {
    Headers headers = new Headers(new Header("Content-Type", "application/json"), new Header("X-Test", "true"));

    // Build RestAssured Response
    Response response = Phase5AllTestsSuite.buildRestAssuredResponse(201, "{\"id\":1,\"name\":\"John\"}", headers);

    // Raw Tool Adapter (RestAssuredAdapter) → ApiContext-neutral ApiResponseAdapter
    ApiResponseAdapter adapter = RestAssuredAdapter.adapt(response);

    // Test Layer → assert only on view (tool & context agnostic)
    // Verify ApiResponseAdapter is not null
    assertNotNull(adapter);

    // Verify raw response is same as raw tool-specific response
    assertSame(
        response,
        adapter.raw(),
        "ApiResponseAdapter raw response should be same as raw tool-specific response");

    assertEquals(201, adapter.statusCode());
    assertEquals("{\"id\":1,\"name\":\"John\"}", adapter.body());
    assertEquals("application/json", adapter.headers().get("Content-Type"));
    assertEquals("true", adapter.headers().get("X-Test"));
  }

  @Test
  void adapt_shouldUseFirstHeaderValue_whenDuplicateHeadersExist() {
    Headers headers = new Headers(new Header("Set-Cookie", "A=1"), new Header("Set-Cookie", "B=1"));

    // Build RestAssured Response
    Response response = Phase5AllTestsSuite.buildRestAssuredResponse(201, "{\"id\":1,\"name\":\"John\"}", headers);

    // Raw Tool Adapter (RestAssuredAdapter) → ApiContext-neutral ApiResponseAdapter
    ApiResponseAdapter adapter = RestAssuredAdapter.adapt(response);

    // Test Layer → assert only on view (tool & context agnostic)
    // Verify ApiResponseAdapter is not null
    assertNotNull(adapter);

    // Verify raw response is same as raw tool-specific response
    assertSame(
        response,
        adapter.raw(),
        "ApiResponseAdapter raw response should be same as raw tool-specific response");

    assertEquals(201, adapter.statusCode());
    assertEquals("{\"id\":1,\"name\":\"John\"}", adapter.body());
    assertEquals("A=1", adapter.headers().get("Set-Cookie"));
  }
}
