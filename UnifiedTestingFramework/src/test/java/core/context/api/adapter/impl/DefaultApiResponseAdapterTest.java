package core.context.api.adapter.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import core.context.api.Phase5AllTestsSuite;

/** - Adapter is pure data - No transformation */
@ExtendWith(Phase5AllTestsSuite.class)
public class DefaultApiResponseAdapterTest {
  @Test
  void constructor_shouldSuccess_whenResponseProvided() {
    // Create DefaultApiResponseAdapter with dummy response
    DefaultApiResponseAdapter adapter = new DefaultApiResponseAdapter(
        200,
        Map.of("Content-Type", "application/json", "Accept", "application/json"),
        "{\"ok\": true}",
        "RAW");

    // Verify fields of ApiResponseAdapter
    assertEquals(200, adapter.statusCode());
    assertEquals("application/json", adapter.headers().get("Accept"));
    assertEquals("application/json", adapter.headers().get("Content-Type"));
    assertEquals("{\"ok\": true}", adapter.body());
    assertEquals("RAW", adapter.raw());
  }
}
