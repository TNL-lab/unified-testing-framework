package api.validators.user;

import static org.hamcrest.Matchers.*;

import api.contract.user.v2.UserFieldsV2;
import io.restassured.response.Response;

/**
 * User V2 specific validations.
 */
public final class UserValidationV2 {
	private UserValidationV2() {
		// TODO Auto-generated constructor stub
	}
	
    /**
     * Validate phone number existence (V2+).
     */
	public static void validatePhone(Response response) {
		response.then().body(UserFieldsV2.PHONE.path(), notNullValue());
	}
}
