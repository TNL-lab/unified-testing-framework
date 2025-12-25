package api.endpoints.user;

import api.client.ApiClient;
import api.config.ApiRequestConfig;
import api.enums.ApiContentType;
import core.utils.LogUtil;
import io.restassured.response.Response;

/**
 * Business-level API actions for User resource..
 *
 * This class represents API object layer
 */
public final class UserEndpoint {

	// Base path for user APIs
	private UserEndpoint() {
		// TODO Auto-generated constructor stub
	}
	
	private static String userEnpoint() {
		return UserPaths.users();
	}
   
	private static String userByIdEnpoint(String id) {
		return UserPaths.userById(id);
	}
	/**
     * GET /users
     */
    public static Response getUsers() {

    	// Build endpoint path
    	String endpoint = userEnpoint();
   	 	
    	// Log endpoint for debugging
        LogUtil.debug("Calling GET request " + endpoint);
        
        // Execute request
        return ApiClient.get(endpoint);
    }
  
    /**
     * GET /users/{id}
     */
    public static Response getUserById(String id) {
    	// Build endpoint path
    	String endpoint = userByIdEnpoint(id);
    	
    	 // Log endpoint for debugging
        LogUtil.debug("Calling GET request " + endpoint);
        
        // Execute request
		return ApiClient.get(endpoint);
	}
    
    /**
     * POST /users
     */
    public static Response createUser(Object body) {
    	// Build endpoint path
    	String endpoint = userEnpoint();
    	
   	 	// Log endpoint for debugging
        LogUtil.debug("Calling POST request " + endpoint);
        
        ApiRequestConfig config =  new ApiRequestConfig();
     // Execute request
    	return ApiClient.post(endpoint, body, config);
	}
    
//    /**
//     * PUT /users/{id}
//     */
//    public static Response updateFullUser(String id,Object body) {
//    	// Build endpoint path
//    	String endpoint = userByIdEnpoint(id);
//    	
//   	 	// Log endpoint for debugging
//        LogUtil.debug("Calling PUT request " + endpoint);
//    	
//        // Execute request
//    	return ApiClient.put(endpoint, body);
//	}
//    
//    /**
//     * PATCH /users/{id}
//     */
//    public static Response updatePartialUser(String id, Object body) {
//    	// Build endpoint path
//    	String endpoint = userByIdEnpoint(id);
//    	
//   	 	// Log endpoint for debugging
//        LogUtil.debug("Calling PATCH request " + endpoint);
//        
//        // Execute request
//    	return ApiClient.patch(endpoint, body);
//	}
//    
//    /**
//     * DELETE /users/{id}
//     */
//    public static Response deleteUser(String id, Object body) {
//    	// Build endpoint path
//    	String endpoint = userByIdEnpoint(id);
//    	
//   	 	// Log endpoint for debugging
//        LogUtil.debug("Calling DELETE request " + endpoint);
//        
//        // Execute request
//    	return ApiClient.delete(endpoint, body);
//	}

}
