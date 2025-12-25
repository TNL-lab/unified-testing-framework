package core.wait;

import org.openqa.selenium.WebDriver;

import core.config.TimeoutConfig;

// Applies timeout configurations to WebDriver.
public final class WaitApplier {
	private WaitApplier() {
	}
	
    /**
     * Apply all configured timeouts to the WebDriver.
     */
	public static void apply(WebDriver webDriver) {
		 // Apply implicit wait for element lookup
		webDriver.manage().timeouts().implicitlyWait(TimeoutConfig.implicitWait());
		// Apply page load timeout
		webDriver.manage().timeouts().pageLoadTimeout(TimeoutConfig.pageLoad());
	}
}
