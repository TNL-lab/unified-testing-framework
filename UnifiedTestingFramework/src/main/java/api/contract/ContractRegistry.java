package api.contract;

import api.contract.user.v1.UserContractValidatorV1;
import api.contract.user.v2.UserContractValidatorV2;

/**
 * Central registry for API contracts.
 */
public final class ContractRegistry {
	private ContractRegistry() {
		// TODO Auto-generated constructor stub
	}
	
	public static BaseContractValidator<?> userV1() {
		return new UserContractValidatorV1();
	}
	
	public static BaseContractValidator<?> userV2() {
		return new UserContractValidatorV2();
	}
}
