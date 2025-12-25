package core.wait;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.FluentWait;

import core.config.TimeoutConfig;

//Factory for creating FluentWait instances.
public final class FluentWaitFactory {
	public FluentWaitFactory() {
	}

	//Create default FluentWait for WebDriver.
	public static FluentWait<WebDriver> forDriver(WebDriver webDriver) {
		// Create a new FluentWait instance bound to the given WebDriver
		// FluentWait allows configurable timeout, polling interval, and ignored exceptions
		return new FluentWait<WebDriver>(webDriver)

				// Set the maximum amount of time to wait before timing out
				// This value is read from configuration (timeout.fluent.total)
				.withTimeout(TimeoutConfig.fluentTimeout())

				// Define how often the condition should be evaluated
				// Shorter polling increases responsiveness but may impact performance
				.pollingEvery(TimeoutConfig.fluentPolling())

				// Ignore NoSuchElementException during polling
				// This is common when elements are not immediately present in the DOM
				.ignoring(NoSuchElementException.class)

				// Ignore StaleElementReferenceException during polling
				// This handles cases where the DOM is refreshed or elements are re-rendered
				.ignoring(StaleElementReferenceException.class);
	}
}
