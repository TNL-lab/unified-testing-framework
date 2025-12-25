package core.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

//Responsible for loading .properties files from resources folder
public class ConfigLoader {
	private static final Properties PROPERTIES = new Properties();

	static {
		// Load common configuration (used by all platforms)
		load("config/config.properties");
		
		// Load API-specific configuration
		load("config/api.properties");
		
		// Load mobile-specific configuration
		load("config/mobile.properties");
	}

	private ConfigLoader() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Load a properties file from classpath
	 *
	 * @param fileName path inside resources (e.g. config/config.properties)
	 * @return Properties object with key-value pairs
	 */
	private static void load(String fileName) {
		// Load file as stream from classpath
		try (InputStream inputStream = ConfigLoader.class.getClassLoader().getResourceAsStream(fileName)) {
			if (inputStream == null) {
				throw new RuntimeException("Config file not found: " + fileName);
			} else {
				PROPERTIES.load(inputStream);
			}
		} catch (IOException e) {
			// Wrap checked exception into runtime exception
			throw new RuntimeException("Failed to load config: " + fileName, e);
		}
	}

	public static Properties getAll() {
		return PROPERTIES;
	}
}