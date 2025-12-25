package core.config;

import api.enums.ContractMode;
import core.enums.PlatformType;
import web.enums.BrowserType;

//Centralized access to environment-related configuration
public final class EnvironmentConfig {
	private EnvironmentConfig() {
		// TODO Auto-generated constructor stub
	}

	// Returns execution platform (WEB / API / MOBILE)
	public static PlatformType getPlatform() {
		return ConfigParser.getEnum("platform", PlatformType.class);
	}

	// Returns browser type for web execution
	public static BrowserType getBrowser() {
		return ConfigParser.getEnum("web.browser", BrowserType.class);
	}

	// Returns execution environment name (e.g. dev, qa, prod).
	public static String getEnviroment() {
		return ConfigParser.getString("env");
	}

	// Check whether test is running in DEBUG mode
	public static boolean isDebugMode() {
		return "debug".equalsIgnoreCase(ConfigParser.getString("test.mode", "ci"));
	}

	/**
	 * Enable pause after test
	 */
	public static boolean isDebugPauseEnabled() {
		return ConfigParser.getBoolean("debug.pause.enabled", false);
	}

	/**
	 * Pause duration (seconds)
	 */
	public static int debugPauseSeconds() {
		return ConfigParser.getInt("debug.pause.seconds", 0);
	}

	/**
	 * Get WEB base URL
	 */
	public static String getWebUrl() {
		return ConfigParser.getString("web.base.url");
	}
	
    /**
     * Get API base URL
     */
	public static String getApiUrl() {
		return ConfigParser.getString("api.base.url");
	}
	
    /**
     * Get API contract mode from configuration.
     * Example values: STRICT, LOOSE, SCHEMA
     */
    public static ContractMode getApiContractMode() {
        return ConfigParser.getEnum("api.contract.mode", ContractMode.class);
    }

    /**
     * Check whether strict validation is enabled.
     */
    public static boolean isStrictModeEnabled() {
        return ConfigParser.getBoolean("strict.enable", false);
    }
}
