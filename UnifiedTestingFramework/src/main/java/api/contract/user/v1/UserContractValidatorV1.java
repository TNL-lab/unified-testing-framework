package api.contract.user.v1;

import api.contract.BaseContractValidator;
import api.contract.CommonContractValidator;
import io.restassured.response.Response;

/**
 * User-specific contract validator.
 *
 */
public final class UserContractValidatorV1 extends BaseContractValidator<UserContractV1>{

	/**
	 * Validate required fields.
	 * 
	 * This validation ensures backward compatibility: 
	 * - These fields MUST always exist 
	 * - Removing them should break tests immediately
	 */
	@Override
	protected void validateRequiredFields(Response response) {
		CommonContractValidator.validateRequiredFields(response,UserContractV1.requiredFields());
	}

	/**
	 * Validate newly introduced fields.
	 *
	 * This validation should be: 
	 * - Optional in loose mode 
	 * - Mandatory in strict mode
	 */
	@Override
	protected void validateNewFields(Response response) {
		// Assert fields added later in V1 and is not null
		//response.then().body(UserFieldsV1.PHONE.path(), notNullValue());
	}

	/**
	 * Validate data types of all User fields based on contract metadata.
	 *
	 * This method: 
	 * - Iterates through centralized field-type mapping 
	 * - Avoids hard-coded assertions 
	 * - Automatically scales when fields are added or removed
	 */
	@Override
	protected void validateFieldTypes(Response response) {
		CommonContractValidator.validateFieldTypes(response,UserContractV1.fieldTypes());
	}
	
	@Override
	protected void validateSchema(Response response) {
		CommonContractValidator.validateSchema(response,"schema/user_v1.schema.json");
	}
	
    /**
     * Validate that response contains NO extra fields
     * beyond the defined contract (STRICT mode).
     */
	@Override
	protected void validateNoExtraFields(Response response) {		
		CommonContractValidator.validateNoExtraFields(response,UserContractV1.allFields());
	}
}
