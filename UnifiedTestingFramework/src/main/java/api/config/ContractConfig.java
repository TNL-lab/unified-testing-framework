package api.config;

import api.enums.ContractMode;
import core.config.EnvironmentConfig;

/**
 * Contract configuration abstraction.
 *
 * This layer converts raw environment configuration
 * into meaningful contract behavior.
 */
public final class ContractConfig {
	private ContractConfig() {
		// TODO Auto-generated constructor stub
	}
	
    /**
     * Get current contract mode.
     */
	public static ContractMode mode() {		
		return EnvironmentConfig.getApiContractMode();
	}
	
    /**
     * Determine whether strict validation should be applied.
     *
     * Strict mode can be enabled explicitly or
     * implicitly via STRICT contract mode.
     */
	public static boolean isStrict() {
		return (EnvironmentConfig.isStrictModeEnabled() && (mode() == ContractMode.STRICT));
	}
	
    /**
     * Check if loose validation is allowed.
     */
	public static boolean isLoose() {
		return (!EnvironmentConfig.isStrictModeEnabled() && (mode() == ContractMode.LOOSE));
	}
	
    /**
     * Check if schema validation is allowed.
     */
	public static boolean isSchema() {
		return (!EnvironmentConfig.isStrictModeEnabled() && (mode() == ContractMode.SCHEMA));
	}
}
