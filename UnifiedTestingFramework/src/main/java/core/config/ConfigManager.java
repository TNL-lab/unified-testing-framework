package core.config;

import java.util.Properties;

/**
 * Central access point for all configuration values Prevents direct access to
 * Properties files across the framework
 */
public class ConfigManager {
	// Load common configuration (used by all platforms)
	private static final Properties CONFIG = ConfigLoader.getAll();

	private ConfigManager() {
		// TODO Auto-generated constructor stub
	}

	// Get common configuration value
	public static String getCommon(String key) {
		return CONFIG.getProperty(key);
	}
}
