package api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import api.client.ApiClient;
import api.contract.ContractRegistry;
import api.endpoints.user.UserEndpoint;
import core.BaseApiTest;
import core.utils.LogUtil;
import io.restassured.response.Response;

public class UserApiTest extends BaseApiTest {

	@Test
	void shouldGetUsersSuccessfully() {
		// Send GET request
		Response response = ApiClient.get(UserEndpoint.getUsers().toString());
		String responseBody = response.getBody().asString();
		LogUtil.info("API status code: "+ response.getStatusCode() + " body: "+responseBody);
		
		// Assert HTTP status		
		assertEquals(200, response.getStatusCode());

		// Assert response body not empty
		assertFalse(responseBody.isEmpty());
	}
	
	@Test
	void user_api_backward_compatibility() {
		Response response = UserEndpoint.getUserById("123");
		
		 // Validate against old contract
		ContractRegistry.userV1().validate(response);
		
		// Validate against new contract
		ContractRegistry.userV2().validate(response);
	}
}
