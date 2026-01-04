package core.context.api;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import core.context.ContextException;
import core.context.TestContext;
import core.context.api.adapter.ApiResponseAdapter;
import core.context.api.adapter.RestAssuredAdapter;
import core.utils.LogUtil;
import io.restassured.RestAssured;
import io.restassured.response.Response;

/**
  * - Builder fail fast
  * - Context only stores the ResponseAdapter without performing any transformation
  */
@ExtendWith(Phase5AllTestsSuite.class) // Use Phase5AllTestsSuite to initialize TestContext
public class ApiContextBuilderTest {

    /**
     * Sets up the test class for ApiContextBuilderTest.
     *
     * Logs a message indicating the start of the test class.
     */
    @BeforeClass
    public void setUp() {
        LogUtil.info("Starting Phase 5 - Test for ApiContextBuilder");
    }

    /**
     * Cleans up the test class for ApiContextBuilderTest.
     *
     * Logs a message indicating the end of the test class.
     */
    @AfterClass
    public void tearDown() {
        LogUtil.info("Ending Phase 5 - Test for ApiContextBuilder");
    }

    /**
     * Test that ApiContextBuilder fails to build when ResponseAdapter is missing.
     *
     * Verify that ContextException is thrown with a message containing "ResponseAdapter".
     */
    @Test
    void build_shouldFail_whenResponseAdapterMissing() {
        // HTTP Client (RestAssured) → raw tool-specific response
        Response response = RestAssured.given()
        .baseUri("https://httpbin.org")
        .when()
        .get("/status/200");

        // Convert RestAssuredResponse into ResponseAdapter
        ApiResponseAdapter adapter = RestAssuredAdapter.adapt(response);

        // Create ApiContextBuilder: assemble ApiContext from normalized response
        ApiContextBuilder builder = new ApiContextBuilder();

        // ApiContextBuilder should fail to build because ResponseAdapter is missing
        ContextException exception = assertThrows(ContextException.class,builder::build);

        // Verify exception message
        assertTrue(exception.getMessage().contains("ResponseAdapter"));
    }

    /**
     * Tests that ApiContextBuilder creates an ApiContext when ResponseAdapter is provided.
     *
     * Verifies all the expected properties of the created ApiContext.
     */
    @Test
    void build_shouldCreateApiContext_whenResponseAdapterProvide() {
        // HTTP Client (RestAssured) → raw tool-specific response
        Response response = RestAssured.given()
        .baseUri("https://httpbin.org")
        .when()
        .get("/status/200");

        // Convert RestAssuredResponse into ResponseAdapter
        ApiResponseAdapter adapter = RestAssuredAdapter.adapt(response);

        // Create ApiContextBuilder: assemble ApiContext from normalized response
        ApiContextBuilder builder = new ApiContextBuilder();

        // DefaultApiContext → hold API execution state & normalized response
        ApiContext apiContext = builder.withResponseAdapter(adapter).build();

        // Verify ApiContext is not null
        assertNotNull(apiContext);

        // Verify ResponseAdapter of ApiContext is same as ApiResponseAdapter after nomalization
        assertSame(adapter, apiContext.response(), "Adapter should be stored correctly");

        // TestContext → store ApiContext for current test lifecycle
        TestContext testContext = Phase5AllTestsSuite.getContextStatic();

        // Store ApiContext in TestContext
        testContext.put(ApiContext.class, apiContext);

        // Retrieve ApiContext from TestContext
        ApiContext retrieveDefaultApiContext = testContext.get(ApiContext.class);

        // Verify retrieved ApiContext is not null
        assertNotNull(retrieveDefaultApiContext, "Retrieved ApiContext should not be null");

        // Verify ResponseAdapter of ApiContext is same as ResponseAdapter after nomalization
        assertEquals(
            adapter,
            retrieveDefaultApiContext.response(),
            "Adapter should match after retrieval of ApiContext from TestContext");

        // Verify detail of ResponseAdapter
        // Verify body
        assertNotNull(apiContext.response().body(), "Body of ResponseAdapter should not be null");

        // Verify body
        assertNotNull(apiContext.response().headers(), "Headers of ResponseAdapter should not be null");

        // Verify statusCode of ResponseAdapter
        assertEquals(200, apiContext.response().statusCode(), "Status code of ResponseAdapter should be 200");
    }
}
