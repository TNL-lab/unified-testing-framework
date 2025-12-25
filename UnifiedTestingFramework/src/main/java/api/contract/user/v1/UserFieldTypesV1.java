package api.contract.user.v1;

import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Map.*;

/**
 * Defines expected data types for User V1 fields. Acts as API contract
 * metadata.
 */
public final class UserFieldTypesV1 {
	// Immutable map: field → expected Java type
	private static Map<UserFieldsV1, Class<?>> FIELD_TYPES = ofEntries(
			entry(UserFieldsV1.ID, String.class),
			entry(UserFieldsV1.NAME, String.class), 
			entry(UserFieldsV1.EMAIL, String.class));

	public UserFieldTypesV1() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Get expected Java type for a given field.
	 */
	public static Class<?> get(UserFieldsV1 userFields) {
		return FIELD_TYPES.get(userFields);
	}

	/**
	 * Expose all field-type mappings.
	 */
	public static Map<UserFieldsV1, Class<?>> all() {
		return FIELD_TYPES;
	}

	/**
	 * Convert to string-key map for diffing.
	 */
	public static Map<String, Class<?>> asStringMap() {
		return all().entrySet().stream()
				.collect(Collectors.toMap(
						e -> e.getKey().path(), 
						Map.Entry::getValue
				));
	}

}
