package core.context;

import org.openqa.selenium.WebDriver;

/**
 * Holds all test-scoped objects. One TestContext per test thread.
 */
public final class WebContextOld {

	// ThreadLocal ensures each test thread has its own TestContext
	private static final ThreadLocal<WebContextOld> CONTEXT = ThreadLocal.withInitial(WebContextOld::new);

	// WebDriver instance for WEB tests
	private WebDriver webDriver;

	// Private constructor prevents external instantiation
	private WebContextOld() {
	}

	/**
	 * Retrieve WebDriver from test context.
	 *
	 * @return WebDriver instance
	 * @throws IllegalStateException if driver not initialized
	 */
	public WebDriver getWebDriver() {
		if (webDriver == null) {
			throw new IllegalStateException("WebDriver is not initialized in TestContext");
		}
		return webDriver;
	}

	/**
	 * Bind WebDriver to current test context.
	 *
	 * @param driver initialized WebDriver
	 */
	public void setWebDriver(WebDriver webDriver) {
		this.webDriver = webDriver;
	}

	/**
	 * Get TestContext bound to the current test thread.
	 *
	 * @return TestContext instance
	 */
	public static WebContextOld getContext() {
		return CONTEXT.get();
	}

	/**
	 * Check whether WebDriver exists in current context.
	 */
	public boolean hasWebDriver() {
		return webDriver != null;
	}

	/**
	 * Clean up all resources associated with current test thread. This method must
	 * be called after each test.
	 */
	public static void clear() {
		WebContextOld testContext = CONTEXT.get();

		// Quit WebDriver if it exists
		if (testContext.webDriver != null) {
			testContext.webDriver.quit();
		}

		// Remove TestContext from ThreadLocal to avoid memory leaks
		CONTEXT.remove();
	}

}
