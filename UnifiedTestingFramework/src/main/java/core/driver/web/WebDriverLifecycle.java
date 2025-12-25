package core.driver.web;

import org.openqa.selenium.WebDriver;

//Handles web driver lifecycle actions.
public final class WebDriverLifecycle {
	
	// Thread-safe WebDriver storage
	private static ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

	
	private WebDriverLifecycle() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Bind WebDriver to current thread
	 */
	public static void set(WebDriver driver) {
		DRIVER.set(driver);
	}
	
	/**
	 * Get WebDriver bound to current thread.
	 *
	 * @return WebDriver instance
	 */
    public static WebDriver get() {
		WebDriver driver = DRIVER.get();
		if (driver == null) {
			throw new IllegalStateException(
					"WebDriver is not initialized for current thread. Did you forget setDriver()?");
		}
		return driver;
    }
	
    /**
     * Quit WebDriver safely and clean ThreadLocal storage. 
     */
	public static void quit() {
		WebDriver webDriver = DRIVER.get();
		if (webDriver != null) {
			webDriver.quit();
			DRIVER.remove();
		}
	}
}
