package api.contract.user.v1;

import java.util.Map;
import java.util.Set;

/**
 * User API contract definition - version 1
 * 
 * Defines:
 *  - Required fields
 *  - Field data types.
 */
public final class UserContractV1 {
	private UserContractV1() {}
	
    /**
     * Required fields for User V1 response.
     */
	public static Set<String> requiredFields() {
		return Set.of(
				UserFieldsV1.ID.path(),
				UserFieldsV1.NAME.path(),
				UserFieldsV1.EMAIL.path()
		);
	}
	
    /**
     * Field → expected Java type mapping.
     */
	public static Map<String, Class<?>> fieldTypes() {
		return UserFieldTypesV1.asStringMap();
	}
	
    /**
     * All allowed fields (used for STRICT mode).
     */
	public static Set<String> allFields() {
		return UserFieldsV1.allPaths();
	}
}
