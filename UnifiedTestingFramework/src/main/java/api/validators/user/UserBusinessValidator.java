package api.validators.user;

import static org.hamcrest.Matchers.*;

import api.contract.user.v1.UserFieldsV1;
import io.restassured.response.Response;

/**
 * Version-independent user business validations.
 *
 * Responsibility: 
 * - Validate User domain response structure 
 * - Validate field existence, data types, and values
 *
 */
public final class UserBusinessValidator {
	private UserBusinessValidator() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Validate user id equals expected value.
	 * 
	 * @param response       API response
	 * @param expectedUserId Expected user ID
	 */
	public static void validateUserId(Response response, String expectedUserId) {
		//Assert that response user id equals expected value
		response.then().body(UserFieldsV1.ID.path(), equalTo(expectedUserId));
	}
	
	/**
	 * Validate email format.
	 * 
	 * @param response   API response
	 */
	public static void validateEmailFormat(Response response) {
		response.then().body(UserFieldsV1.EMAIL.path(), containsString("@"));
	}	
}
