package api.utils;

import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;

import core.utils.JsonUtils;
import io.restassured.response.Response;

/**
 * API-specific JSON extractor.
 * Converts Response → JSON fields.
 */
public final class JsonFieldExtractor {
	
	private JsonFieldExtractor() {}
	
    /**
     * Extract root JSON fields from API response.
     */
	public static Set<String> extractRootFields(Response response) {
		// Parse response body into JsonNode
		String responseString = response.asString();
		JsonNode jsonNode = JsonUtils.parse(responseString);

		return JsonUtils.getRootFieldNames(jsonNode);
	}
 
}
