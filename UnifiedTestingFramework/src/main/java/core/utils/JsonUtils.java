package core.utils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Core JSON utilities. This class is framework-agnostic and API-agnostic.
 */
public final class JsonUtils {

	// Shared ObjectMapper instance
	private static final ObjectMapper MAPPER = new ObjectMapper();

	private JsonUtils() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Parses raw JSON string into JsonNode.
	 */
	public static JsonNode parse(String json) {
		try {
			return MAPPER.readTree(json);
		} catch (Exception e) {
			throw new RuntimeException("Failed to parse JSON", e);
		}
	}

	/**
	 * Extracts root-level field names from JsonNode.
	 *
	 * @param response API response
	 * @return set of field names
	 */
	public static Set<String> getRootFieldNames(JsonNode jsonNode) {
		Set<String> fields = new HashSet<>();

		// Iterate through JSON field names
		Iterator<String> iterator = jsonNode.fieldNames();

		// Collect all field names
		iterator.forEachRemaining(fields::add);

		return fields;
	}
}
