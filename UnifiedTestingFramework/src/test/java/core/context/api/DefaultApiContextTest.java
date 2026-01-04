package core.context.api;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import core.context.ContextException;
import core.context.api.adapter.ApiResponseAdapter;
import core.context.api.adapter.RestAssuredAdapter;
import core.utils.LogUtil;
import io.restassured.RestAssured;
import io.restassured.response.Response;

/**
 * - Context is immutable holder
 * - No hidden logic
 */
@ExtendWith(Phase5AllTestsSuite.class)
public class DefaultApiContextTest {
    @BeforeClass
    public void setUp() {
        LogUtil.info("Starting DefaultApiContextTest of Phase 5");
    }

    @AfterClass
    public void tearDown() {
        LogUtil.info("Ending DefaultApiContextTest of Phase 5");
    }

    @Test
    void constructor_shouldFail_whenResponseAdapterIsMissing() {
        // Attempt to create DefaultApiContext with ResponseAdapter is missing
        Exception ex = assertThrows(
            ContextException.class,
            () -> new DefaultApiContext(null));

        // Verify the exception message
        assertEquals(
            "Response Adapter must not be null",
            ex.getMessage(),
            " Expected ContextException message to be 'Response Adapter must not be null'");
        assertTrue(
            ex.getMessage().contains("must not be null"),
            " Expected ContextException message to contain 'must not be null'");
    }

    @Test
    void constructor_shouldSucceed_whenResponseAdapterProvided() {
        // HTTP Client (RestAssured) → raw tool-specific response
        Response response = RestAssured.given()
        .baseUri("https://httpbin.org")
        .when()
        .get("/status/200");

        //Raw Tool Adapter (RestAssuredAdapter) → ApiContext-neutral ApiResponseAdapter
        ApiResponseAdapter adapter = RestAssuredAdapter.adapt(response);

        // Verify raw response is same as raw tool-specific response
        assertEquals(
            response,
            adapter.raw(),
            "ApiResponseAdapter raw response should be same as raw tool-specific response"
        );

        // Create DefaultApiContext
        DefaultApiContext defaultApiContext = new DefaultApiContext(adapter);

        // Test Layer → assert only on view (tool & context agnostic)
        // Verify raw response is same as ApiResponseAdapter raw response
        assertEquals(
            adapter.raw(),
            defaultApiContext.response().raw(),
            " DefaultApiContext response should be same as ApiResponseAdapter raw response");

        // Verify fields of ResponseAdapter
        assertEquals(200, defaultApiContext.response().statusCode(), "DefaultApiContext status code should be 200");
        assertNotNull(defaultApiContext.response().headers(), "DefaultApiContext headers should not be null");
        assertNotNull(defaultApiContext.response().body(), "DefaultApiContext body should not be null");
    }
}
