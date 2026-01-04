package core.context.api.adapter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import core.context.api.Phase5AllTestsSuite;
import core.utils.LogUtil;

/**
 * - Adapter is pure data
 * - No transformation
 */
@ExtendWith(Phase5AllTestsSuite.class)
public class ApiResponseAdapterTest {
     /**
     * Sets up the test class for ApiResponseAdapterTest.
     *
     * Logs a message indicating the start of the test class.
     */
    @BeforeClass
    public void setUp() {
        LogUtil.info("Starting Phase 5 - Test for ApiResponseAdapter");
    }

    /**
     * Cleans up the test class for ApiResponseAdapterTest.
     *
     * Logs a message indicating the end of the test class.
     */
    @AfterClass
    public void tearDown() {
        LogUtil.info("Ending Phase 5 - Test for ApiResponseAdapter");
    }

    @Test
    void constructor_shouldSuccess_whenResponseProvided() {
        // Create ApiResponseAdapter with dummy response
        ApiResponseAdapter adapter = new ApiResponseAdapter(
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
