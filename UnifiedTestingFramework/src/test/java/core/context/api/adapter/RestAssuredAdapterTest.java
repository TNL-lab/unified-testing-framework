package core.context.api.adapter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import api.enums.ApiContentType;
import core.context.adapter.ResponseAdapter;
import core.context.api.Phase5AllTestsSuite;
import core.utils.LogUtil;
import io.restassured.builder.ResponseBuilder;
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
    /**
     * Sets up the test class for RestAssuredAdapterTest.
     *
     * Logs a message indicating the start of the test class.
     */
    @BeforeClass
    public void setUp() {
        LogUtil.info("Starting Phase 5 - Test for RestAssuredAdapter");
    }

    /**
     * Cleans up the test class for RestAssuredAdapterTest.
     *
     * Logs a message indicating the end of the test class.
     */
    @AfterClass
    public void tearDown() {
        LogUtil.info("Ending Phase 5 - Test for RestAssuredAdapter");
    }

    @Test
    void adapt_shouldSuccess_whenRestAssuredResponseProvided() {
        // Build RestAssured Response
        Response response = new ResponseBuilder().setStatusCode(201)
        .setBody("{\"id\":1,\"name\":\"John\"}")
        .setContentType(ApiContentType.JSON.value())
        .setHeader("X-Test", "true")
        .build();

        // Raw Tool Adapter (RestAssuredAdapter) → ApiContext-neutral ApiResponseAdapter
        ResponseAdapter adapter = RestAssuredAdapter.adapt(response);

        // Test Layer → assert only on view (tool & context agnostic)
        // Verify ApiResponseAdapter is not null
        assertNotNull(adapter);

        // Verify raw response is same as raw tool-specific response
        assertSame(
            response,
            adapter.raw(),
            "ApiResponseAdapter raw response should be same as raw tool-specific response"
        );

        // Verify filelds of ApiResponseAdapter
        assertEquals(201, adapter.statusCode());
        assertEquals("{\"id\":1,\"name\":\"John\"}", adapter.body());
        assertEquals("true", adapter.headers().get("X-Test"));
    }
}