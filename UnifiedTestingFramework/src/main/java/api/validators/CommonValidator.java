package api.validators;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;

import api.constants.HttpHeaders;
import api.enums.HttpStatus;
import io.restassured.response.Response;

/**
 * Common reusable API validators.
 *
 * This class contains generic validations that can be applied
 * to any API response regardless of domain (user, product, order, etc).
 */
public final class CommonValidator {
	private CommonValidator() {
		// TODO Auto-generated constructor stub
	}

    /**
     * Validate HTTP status code.
     *
     * @param response   API response object
     * @param status     Expected HTTP status (enum)
     */
	public static void validateStatus(Response response, HttpStatus httpStatus) {
		// Assert that response status code matches expected value
		response.then().statusCode(httpStatus.code());
	}
	
    /**
     * Validate Content-Type header.
     *
     * @param response     API response object
     * @param contentType  Expected content type enum
     */
	public static void validateContentType(Response response, String apiContentType) {
		 // Validate that Content-Type header equals expected value
		response.then().header(HttpHeaders.CONTENT_TYPE, containsString(apiContentType));
	}
	
    /*
     * Validate response time is within expected threshold.
     *
     * @param response API response
     * @param maxTimeMs Maximum acceptable response time in milliseconds
     */
	public static void validateResponseTime(Response response, long maxTimeMs) {
		// Assert that response time is less than or equal to defined threshold
		response.then().time(lessThanOrEqualTo(maxTimeMs));
	}
	
    /**
     * Validate response body is not null.
     *
     * @param response API response
     */
	public static void validateBodyNotNull(Response response) {
		// Ensure response body exists
		response.then().body(notNullValue());
	}
	
}
