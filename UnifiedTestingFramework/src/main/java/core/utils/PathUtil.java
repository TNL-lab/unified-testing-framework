package core.utils;

/**
 * Utility class for resolving important project paths. Avoids hardcoding file system paths.
 */
public final class PathUtil {

	public PathUtil() {
		// TODO Auto-generated constructor stub
	}

	// Returns the root directory of the project.
	public static String projectRoot() {
		// user.dir is set by JVM to the working directory
		return System.getProperty("user.dir");
	}

	// Returns the reports directory path
	public static String reportDir() {
		return projectRoot() + "/reports";
	}

	// Returns the test data directory path.
	public static String testDataDir() {
		return projectRoot() + "/src/test/resources/testdata";
	}
}
