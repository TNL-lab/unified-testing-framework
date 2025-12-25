package api.contract;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Map;
import java.util.Set;

import api.utils.JsonFieldExtractor;
import io.restassured.response.Response;

/**
 * Common contract-level validation utilities.
 *
 * Contains ONLY generic contract assertions.
 * No domain-specific knowledge.
 */
public final class CommonContractValidator {
	private CommonContractValidator() {
		// TODO Auto-generated constructor stub
	}

    /**
     * Validate required fields existence.
     *
     * @param response API response
     * @param requiredFields set of required JSON paths
     */
	public static void validateRequiredFields(Response response, Set<String> requiredFields) {
		// Validate that user object contains required fields
		for (String field : requiredFields) {
			// Assert that required field exists and is not null
			response.then().body(field, notNullValue()); 
		}
	}

    /**
     * Validate field data types.
     *
     * @param response API response
     * @param fieldTypes mapping of field path -> expected Java type
     */
	public static void validateFieldTypes(Response response, Map<String, Class<?>> fieldTypes) {
		// Iterate through all field-type mappings
		fieldTypes.forEach((field, type) ->	
			// Assert each field matches expected data type
			response.then().body(field, instanceOf(type)));		
	}

    /**
     * Validates response against JSON schema.
     */
	public static void validateSchema(Response response, String schemaPath) {
		// Validate response using JSON schema
		response.then().assertThat()
				.body(matchesJsonSchemaInClasspath(schemaPath));			
	}

	public static void validateNoExtraFields(Response response, Set<String> allExpectedFields) {
		// Extract actual fields from API response
		Set<String> actualFields = JsonFieldExtractor.extractRootFields(response);
		
		// Remove all expected fields, remaining are extra
		actualFields.removeAll(allExpectedFields);
		
		// Fail if any unexpected fields remain
		if (!actualFields.isEmpty()) {
			throw new AssertionError("Unexpected fields detected: " + actualFields);
		}
		
	}
}
