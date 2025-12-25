package core.utils;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Central logging utility Avoid direct System.out.println scattered across code
 */
public final class LogUtil {
	// Prevent instantiation
	private LogUtil() {

	}

	/**
	 * Get SLF4J logger for a specific class.
	 */
	public static Logger getLogger(Class<?> clazz) {
		return LoggerFactory.getLogger(clazz);
	}

	/**
	 * Log debug message. Use when: Technical details, low-level actions (framework
	 * / BasePage)
	 *
	 * @param message log message
	 */
	public static void debug(String msg) {
		System.out.println(LocalDateTime.now() + " [DEBUG] " + msg);
	}

	/**
	 * Log info message. Use when: Business flow, high-level test steps
	 * 
	 * @param message log message
	 */
	public static void info(String msg) {
		System.out.println(LocalDateTime.now() + " [INFO ] " + msg);
	}

	/**
	 * Log error message. Use when: Serious error causing test failure
	 * 
	 * @param message log message
	 */
	public static void error(String msg) {
		System.err.println(LocalDateTime.now() + " [ERROR] " + msg);
	}

	/**
	 * Log error with Exception Use when: Catching exception that will be re-thrown,
	 * full debug information
	 * 
	 * @param message log message
	 */
	public static void error(String msg, Throwable e) {
		System.err.println(LocalDateTime.now() + " [ERROR] " + msg);
		e.printStackTrace();
	}

	/**
	 * Log warn message. Use when: Unexpected but acceptable behavior
	 * 
	 * @param message log message
	 */
	public static void warn(String msg) {
		System.out.println(LocalDateTime.now() + " [WARN ] " + msg);
	}

}
