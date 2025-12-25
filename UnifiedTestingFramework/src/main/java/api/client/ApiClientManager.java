package api.client;

import api.config.ApiRequestConfig;
import core.config.EnvironmentConfig;
import static io.restassured.RestAssured.given;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

/**
 * Central factory for providing ApiClient instances.
 *
 * Responsibility: - Decide WHICH ApiClient to use (json / xml / form /
 * multipart) - Apply correct base RequestSpecification
 */
public final class ApiClientManager {
	// Prevent instantiation
	private ApiClientManager() {
	}

	/**
	 * Create base RequestSpecification. Contains only environment-level
	 * configuration.
	 */
	public static RequestSpecification baseSpecification() {
		return new RequestSpecBuilder().setBaseUri(EnvironmentConfig.getApiUrl()) // Set API base URL from config
				.setRelaxedHTTPSValidation() // Disable SSL validation for non-prod environments
				.log(io.restassured.filter.log.LogDetail.ALL) // Enable logging when validation fails
				.build(); // Build immutable specification
	}

	/**
	 * Create RequestSpecification from request configuration.
	 *
	 * @param config request-level configuration
	 * @return fully configured RequestSpecification
	 */
	public static RequestSpecification from(ApiRequestConfig apiRequestConfig) {
		// Start from base environment specification
		RequestSpecification requestSpecification = given().spec(baseSpecification()) // Apply base specification
				.log().all(); // Log request for debugging

		// Apply request Content-Type
		requestSpecification.contentType(apiRequestConfig.getContentType().value());

		// Apply Accept header
		requestSpecification.accept(apiRequestConfig.getAccept().value());

		// Apply custom headers
		apiRequestConfig.getHeaders().forEach(requestSpecification::header);

		return requestSpecification;
	}

	/**
	 * Default JSON request specification.
	 *
	 * Used by most API calls.
	 */
	public static RequestSpecification json() {
		return from(new ApiRequestConfig());

	}
}
