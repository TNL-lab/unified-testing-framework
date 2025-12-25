package core.utils;

import java.io.InputStream;

//Utility for safely loading resources from the classpath.
public final class ResourceUtil {

	// Prevent object creation
	private ResourceUtil() {
	}

	/**
	 * Loads a resource file as an InputStream.
	 *
	 * @param path path to resource
	 * @return InputStream of the resource
	 */
	public static InputStream load(String path) {
		// Load resource using class loader
		InputStream inputStream = ResourceUtil.class.getClassLoader().getResourceAsStream(path);
		if (inputStream == null) {
			throw new RuntimeException("Resource not found: " + path);
		}
		return inputStream;
	}

}
