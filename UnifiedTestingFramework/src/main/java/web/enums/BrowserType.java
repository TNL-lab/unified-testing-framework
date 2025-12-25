package web.enums;

//Supported browser types for web automation.
public enum BrowserType {
	CHROME, FIREFOX, EDGE;

//	/**
//	 * Converts string value to BrowserType.
//	 *
//	 * @param value browser name from config
//	 * @return BrowserType enum
//	 */
//	public static BrowserType from(String value) {
//		try {
//			 // Normalize input and map to enum
//			return BrowserType.valueOf(value.toUpperCase());
//		} catch (Exception e) {
//			throw new IllegalArgumentException("Unsupported browser: " + value);
//		}
//	}
}
