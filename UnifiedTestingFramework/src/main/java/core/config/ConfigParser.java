package core.config;

import java.time.Duration;
import java.util.Locale;

/**
 * Utility class responsible for parsing configuration values into
 * strongly-typed objects.
 *
 * This class centralizes all parsing and validation logic to avoid duplicated
 * and unsafe parsing code.
 */
public final class ConfigParser {
	// Private constructor prevents instantiation
	private ConfigParser() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Read raw value from configuration (may be null).
	 */
	private static String readRaw(String key) {
		String rawValue = ConfigManager.getCommon(key);

		// Return trimmed value
		return rawValue.trim();
	}

	/**
	 * Reads and validates a raw configuration value.
	 *
	 * @param key configuration key
	 * @return validated non-blank string value
	 */
	private static String getRequiredValue(String key) {
		// Read raw value from configuration
		String value = readRaw(key);

		// Validate that the configuration key exists
		if (value == null || value.isBlank()) {
			throw new IllegalStateException("Missing or empty config value for key: " + key);
		}

		return value;
	}

	/**
	 * Reads a configuration value and parses it as an integer.
	 *
	 * @param key configuration key
	 * @return parsed integer value
	 */
	public static int getInt(String key) {
		// Get validated value
		String value = getRequiredValue(key);

		return parseInt(value, key);
	}

	/**
	 * Reads a configuration value and returns it as a integer. If the key is
	 * missing or blank, returns the provided default value.
	 *
	 * @param key          configuration key
	 * @param defaultValue value to return if key is missing or blank
	 * @return resolved integer value
	 */
	public static int getInt(String key, int defaultValue) {
		// Read raw value from configuration
		String value = readRaw(key);

		// Return default value if missing or blank
		if (value == null || value.isBlank()) {
			return defaultValue;
		}

		return parseInt(value, key);
	}

	private static int parseInt(String value, String key) {
		try {
			// Parse integer from string
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			// Fail fast with detailed error message
			throw new IllegalStateException("Invalid integer value for key: " + key + ", actual value: " + value, e);
		}
	}

	/**
	 * Reads a configuration value and parses it as an long.
	 *
	 * @param key configuration key
	 * @return parsed integer value
	 */
	public static long getLong(String key) {
		// Get validated value
		String value = getRequiredValue(key);

		return parseLong(value, key);
	}

	/**
	 * Reads a configuration value and returns it as a long. If the key is missing
	 * or blank, returns the provided default value.
	 *
	 * @param key          configuration key
	 * @param defaultValue value to return if key is missing or blank
	 * @return resolved long value
	 */
	public static long getLong(String key, long defaultValue) {
		// Read raw value from configuration
		String value = readRaw(key);

		// Return default value if missing or blank
		if (value == null || value.isBlank()) {
			return defaultValue;
		}

		return parseLong(value, key);
	}

	private static long parseLong(String value, String key) {
		try {
			// Parse long from string
			return Long.parseLong(value);
		} catch (NumberFormatException e) {
			// Fail fast with detailed error message
			throw new IllegalStateException("Invalid long value for key: " + key + ", actual value: " + value, e);
		}
	}

	/**
	 * Reads a configuration value and parses it as a boolean.
	 *
	 * @param key configuration key
	 * @return parsed boolean value
	 */
	public static boolean getBoolean(String key) {
		// Get validated raw value
		String value = getRequiredValue(key);

		// Convert string value to boolean
		return Boolean.parseBoolean(value);
	}

	/**
	 * Reads a configuration value and returns it as a boolean. If the key is
	 * missing or blank, returns the provided default value.
	 *
	 * @param key          configuration key
	 * @param defaultValue value to return if key is missing or blank
	 * @return resolved boolean value
	 */
	public static boolean getBoolean(String key, boolean defaultValue) {
		// Read raw value from configuration
		String value = readRaw(key);

		// Return default value if missing or blank
		if (value == null || value.isBlank()) {
			return defaultValue;
		}

		// Return parse boolean from string
		return Boolean.parseBoolean(value);
	}

	/**
	 * Reads a configuration value and returns it as a string.
	 *
	 * @param key configuration key
	 * @return trimmed string value
	 */
	public static String getString(String key) {
		// Return validated string value
		return getRequiredValue(key);
	}

	/**
	 * Reads a configuration value and returns it as a long. If the key is missing
	 * or blank, returns the provided default value.
	 *
	 * @param key          configuration key
	 * @param defaultValue value to return if key is missing or blank
	 * @return resolved long value
	 */
	public static String getString(String key, String defaultValue) {
		// Read raw value from configuration
		String value = readRaw(key);

		// Return default value if missing or blank
		if (value == null || value.isBlank()) {
			return defaultValue;
		}

		// Return value
		return value.trim();
	}

	/**
	 * Reads a configuration value and parses it as a Duration.
	 *
	 * Supported formats: - 10 -> 10 seconds - 10s -> 10 seconds - 500ms -> 500
	 * milliseconds - 2m -> 2 minutes - 1h -> 1 hour - PT30S -> ISO-8601 Duration
	 * format
	 *
	 * @param key configuration key
	 * @return parsed Duration
	 */
	public static Duration getDuration(String key) {
		// Get validated raw value
		String rawValue = getRequiredValue(key);

		// Normalize value for easier parsing (case-insensitive)
		String value = rawValue.toLowerCase(Locale.ROOT);

		try {
			// ISO-8601 duration format (e.g. PT30S)
			if (value.startsWith("pt")) {
				// Create Duration
				return Duration.parse(rawValue);
			}
			// Milliseconds
			else if (value.endsWith("ms")) {
				// Parse milliseconds
				long milliSeconds = parseTimeValue(value, "ms", key);

				// Create Duration from milliseconds
				return Duration.ofMillis(milliSeconds);
			}
			// Seconds
			else if (value.endsWith("s")) {
				// Parse seconds
				long seconds = parseTimeValue(value, "s", key);

				// Create Duration from seconds
				return Duration.ofSeconds(seconds);
			}
			// Minutes
			else if (value.endsWith("m")) {
				// Parse minutes
				long minutes = parseTimeValue(value, "m", key);

				// Create Duration from minutes
				return Duration.ofMinutes(minutes);
			}
			// Hours
			else if (value.endsWith("h")) {
				// Parse hours
				long hours = parseTimeValue(value, "h", key);

				// Create Duration from hours
				return Duration.ofHours(hours);
			}
			// Default value
			else {
				// Parse value as seconds
				long seconds = parseLong(value, key);

				// Create Duration from seconds
				return Duration.ofSeconds(seconds);
			}
		} catch (Exception e) {
			throw new IllegalStateException("Invalid duration format for key: " + key + ", value: " + rawValue
					+ ". Supported formats: 10, 10s, 500ms, 2m, 1h, PT30S", e);
		}
	}

	private static long parseTimeValue(String value, String unit, String key) {
		// Remove unit suffix
		String numericPart = value.replace(unit, "");

		return parseLong(numericPart, key);
	}

	/**
	 * Reads a configuration value and parses it into an enum.
	 *
	 * @param key       configuration key
	 * @param enumClass enum type
	 * @return parsed enum value
	 */
	public static <T extends Enum<T>> T getEnum(String key, Class<T> enumClass) {
		// Get validated raw value
		String rawValue = getRequiredValue(key);

		// Normalize value for easier parsing (case-insensitive)
		String value = rawValue.toUpperCase();
		try {
			// Convert string value to enum constant
			return Enum.valueOf(enumClass, value);
		} catch (Exception e) {
			throw new IllegalStateException("Invalid value for key: " + key + ", value: " + rawValue
					+ ", expected one of: " + java.util.Arrays.toString(enumClass.getEnumConstants()), e);
		}

	}

}
