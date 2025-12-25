package api.client;

import api.config.ApiRequestConfig;
import core.utils.LogUtil;
import io.restassured.response.Response;

/**
 * Low-level HTTP client wrapper around RestAssured
 * 
 * Responsible for:
 * - HTTP method execution, Transport, HTTP Logic
 * - Applying request specification
 * - Logging request & response
 *
 */
public final class ApiClient {
	private ApiClient() {
		// TODO Auto-generated constructor stub
	}
	
    /**
     * Execute GET request.
     *
     * @param path API endpoint path
     * @return raw HTTP response
     */
	public static Response get(String endpoint) {
		LogUtil.info("GET request to: " + endpoint);
		return ApiClientManager.json()
				.when() // Start request execution
				.get(endpoint) // Send GET request
                .then() // Start response validation chain
                .log().ifValidationFails() // Log response if validation fails
                .extract() // Extract response
                .response(); // Return Response object
	}
	
    /**
     * Execute HTTP POST request with body.
     */
	public static Response post(String endpoint, Object body, ApiRequestConfig config) {
		LogUtil.info("POST request to: " + endpoint);

		return ApiClientManager.from(config)				  
				.body(body) // Attach request body
				.when() // Start request execution
				.post(endpoint) // Send POST request
                .then() // Start response validation chain
                .log().ifValidationFails() // Log response if validation fails
                .extract() // Extract response
                .response(); // Return Response object
	}
	
	 /**
     * Execute HTTP PUT request with body
     */
	public static Response put(String endpoint, Object body, ApiRequestConfig config) {
		LogUtil.info("PUT request to: " + endpoint);
	
		return ApiClientManager.from(config)				  
				.body(body) // Attach request body
				.when() // Start request execution
				.put(endpoint) // Send PUT request
                .then() // Start response validation chain
                .log().ifValidationFails() // Log response if validation fails
                .extract() // Extract response
                .response(); // Return Response object
	}
	
	 /**
     * Execute HTTP PATCH request with body
     */
	public static Response patch(String endpoint, Object body, ApiRequestConfig config) {
		LogUtil.info("PATCH request to: " + endpoint);
		return ApiClientManager.from(config)					  
				.body(body) // Attach request body
				.when() // Start request execution
				.patch(endpoint) // Send PATCH request
                .then() // Start response validation chain
                .log().ifValidationFails() // Log response if validation fails
                .extract() // Extract response
                .response(); // Return Response object
	}
	
	 /**
     * Execute HTTP DELETE request
     */
	public static Response delete(String endpoint) {
		LogUtil.info("DELETE request to: " + endpoint);
		return ApiClientManager.json()			  
				.when() // Start request execution
				.delete(endpoint) // Send DELETE request
                .then() // Start response validation chain
                .log().ifValidationFails() // Log response if validation fails
                .extract() // Extract response
                .response(); // Return Response object
	}
}
