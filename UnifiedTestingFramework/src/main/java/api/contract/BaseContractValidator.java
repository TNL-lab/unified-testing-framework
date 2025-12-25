package api.contract;

import api.config.ContractConfig;
import api.enums.ContractMode;
import io.restassured.response.Response;

/**
 * Base class for all API contracts . 
 * All API contract validators should extend this class.
 * 
 * Handles:
 *  - Strict / Loose / Schema strategy
 *  
 * @param <T> Field enum type (e.g. UserFieldsV1)
 */
public abstract class BaseContractValidator<T> {

	/**
	 * Validate response according to current contract mode.
	 */
	public final void validate(Response response) {

		ContractMode mode = ContractConfig.mode();

		switch (mode) {
		case STRICT -> {
			// validate required fields
			validateRequiredFields(response);

			// Validate types
			validateFieldTypes(response);

			// Validate no extra field
			validateNoExtraFields(response);
		}
		case LOOSE -> {
			// validate required fields
			validateRequiredFields(response);

			// Validate types
			validateFieldTypes(response);
		}

		case SCHEMA -> {
			// Validate schema
			validateSchema(response);
		}

		default -> throw new IllegalStateException("Unsupported contract mode: " + mode);
		}
	}

	/**
	 * Validate mandatory fields.
	 */
	protected abstract void validateRequiredFields(Response response);

	/**
	 * Validate newly introduced fields.
	 */
	protected abstract void validateNewFields(Response response);

	/**
	 * Validate data types of fields.
	 */
	protected abstract void validateFieldTypes(Response response);

	/**
	 * Validate that no unexpected fields are present.
	 */
	protected abstract void validateNoExtraFields(Response response);

	/**
	 * Validate using JSON Schema.
	 */
	protected abstract void validateSchema(Response response);

}
