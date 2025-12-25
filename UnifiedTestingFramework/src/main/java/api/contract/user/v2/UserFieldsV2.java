package api.contract.user.v2;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum UserFieldsV2 {
	PHONE("phone");
	
	// JSON path of the field
	private final String path;
	
	UserFieldsV2(String path){
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
				.map(UserFieldsV2::path)
				.collect(Collectors.toSet());
	}
}
