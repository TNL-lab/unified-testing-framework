package api.contract.user.v1;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Central definition of User JSON fields.
 * Avoids hard-coded strings across validators.
 */
public enum UserFieldsV1 {
	
	ID("id"), 
	NAME("name"), 
	EMAIL("email"), 
	PHONE("phone");
	
    // JSON path of the field
	private final String path;
	
	UserFieldsV1(String path) {
		this.path = path;
	}
	
    /**
     * Returns JSON path of the field.
     */
	public String path() {
		return path;
	}
	 
	/**
     * Returns all field paths
     */
	public static Set<String> allPaths() {
		return Stream.of(values())
				.map(UserFieldsV1::path)
				.collect(Collectors.toSet());
	}
}
