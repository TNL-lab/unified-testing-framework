package api.endpoints.user;

/**
 * Centralized path definitions for User API
 * This class ONLY builds endpoint paths
 */
public final class UserPaths {
	private UserPaths() {
		// TODO Auto-generated constructor stub
	}
	
	// Base path for user APIs
	private static final String USERS = "/users";
	
    /**
     * Path: /users
     */
	public static String users() {
		return USERS;
	}
	
    /**
     * Path: /users/{id}
     *
     * @param userId user identifier (String / UUID / numeric string)
     */
	public static String userById(String id) {
		return USERS + "/" + id;
	}
}
