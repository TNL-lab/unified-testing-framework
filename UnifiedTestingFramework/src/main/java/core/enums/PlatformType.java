package core.enums;

//Supported execution platforms.
public enum PlatformType {
	WEB, // Web UI automation
	API, // API automation
	MOBILE; // Mobile automation

//	/**
//	 * Converts string value to PlatformType.
//	 *
//	 * @param value platform name from config
//	 * @return PlatformType enum
//	 */
//	public static PlatformType from(String value) {
//		try {
//			// Normalize input and map to enum
//			return PlatformType.valueOf(value.toUpperCase());
//		} catch (Exception e) {
//			throw new IllegalArgumentException("Unsupported platform: " + value);
//		}
//	}
}
