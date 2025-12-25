package api.contract.snapshot;

import java.util.Map;
import java.util.Set;

/**
 * Immutable snapshot of an API response structure.
 *
 * This class represents the "observed contract" extracted from an actual API response at runtime.
 *
 * It is used to:
 *  - Detect extra fields
 *  - Detect missing fields
 *  - Detect field type changes
 *  - Support backward compatibility validation
 */
public final class ContractSnapshot {
	// Set of all field paths found in the response.
	private final Set<String> fields;

	// Map of field path -> detected Java type.
	private final Map<String, Class<?>> fieldTypes;

    /**
     * Private constructor to enforce immutability.
     *
     * Snapshot must be created via factory method.
     */
	private ContractSnapshot(Set<String> fields, Map<String, Class<?>> fieldTypes) {
		// Store all detected field paths
		this.fields = fields;
		
		// Store detected data types for each field
		this.fieldTypes = fieldTypes;
	}
	
    /**
     * Factory method to create a snapshot instance.
     *
     * @param fields     all detected JSON field paths
     * @param fieldTypes detected field -> type mapping
     * @return immutable ContractSnapshot
     */
	public static ContractSnapshot of(Set<String> fields, Map<String, Class<?>> fieldTypes) {
		// Create a new immutable snapshot instance
		return new ContractSnapshot(fields, fieldTypes);
	}

    /**
     * Returns all detected field paths.
     *
     * @return set of field paths
     */
	public Set<String> fields() {
		return fields;
	}

    /**
     * Returns detected field-type mappings.
     *
     * @return map of field -> Java type
     */
	public Map<String, Class<?>> fieldTypes() {
		return fieldTypes;
	}
	
    /**
     * Checks whether the response contains a specific field.
     *
     * @param field JSON field path
     * @return true if field exists in response
     */
	public boolean containField(String field) {
		 // Check presence of field path in snapshot
		return fields.contains(field);
	}
	
    /**
     * Returns detected Java type of a field.
     *
     * @param field JSON field path
     * @return detected Java type or null if field not present
     */
	public Class<?> typeOf(String field){
        // Return detected type for the given field
		return fieldTypes.get(field);
	}


}
