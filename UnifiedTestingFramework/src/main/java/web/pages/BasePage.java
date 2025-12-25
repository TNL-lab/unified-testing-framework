package web.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;

import core.context.WebContextOld;
import core.driver.web.WebDriverLifecycle;
import core.driver.web.WebDriverManager;
import core.utils.LogUtil;
import core.wait.FluentWaitFactory;

/**
 * BasePage is the parent class of all Page Objects.
 *
 * Responsibilities: - Hold WebDriver instance - Provide common Selenium actions
 * - Centralize waiting logic
 */
public abstract class BasePage {
	// WebDriver instance bound to current test thread
	protected WebDriver webDriver;

	// FluentWait instance for safe element interaction
	protected FluentWait<WebDriver> fluentWait;

	/**
	 * BasePage constructor.
	 * 
	 * Initializes: - WebDriver from TestContext - FluentWait
	 * with global timeout & polling config
	 */
	protected BasePage() {
		// Retrieve WebDriver from TestContext
		this.webDriver = WebContextOld.getContext().getWebDriver();

		// Create FluentWait instance using centralized factory
		this.fluentWait = FluentWaitFactory.forDriver(webDriver);
	}

	/**
	 * Find a WebElement using FluentWait.
	 *
	 * @param locator Selenium By locator
	 * @return visible WebElement
	 */
	protected WebElement find(By locator) {
		// Log element lookup
		LogUtil.debug("Finding element: " + locator);

		// Wait until element is present in DOM and return it
		return fluentWait.until(webDriver -> locate(locator));
	}

	/**
	 * Low-level element lookup.
	 *
	 * This method isolates driver.findElement() so it can be reused, logged, or
	 * extended later.
	 *
	 * @param locator Selenium By locator
	 * @return raw WebElement
	 */
	protected WebElement locate(By locator) {
		// Direct call to WebDriver to locate element
		return webDriver.findElement(locator);
	}

	/**
	 * Click on an element safely.
	 *
	 * @param locator Selenium By locator
	 */
	protected void click(By locator) {
		// Log click action
		LogUtil.debug("Click element: " + locator);

		// Find element first, then perform click
		find(locator).click();
	}

	/**
	 * Type text into input field.
	 *
	 * @param locator Selenium By locator
	 * @param text    text to input
	 */
	protected void type(By locator, String text) {
		// Log typing action (do NOT log sensitive data in real projects)
		LogUtil.debug("Typing text into element: " + locator);

		// Find input field
		WebElement webElement = find(locator);

		// Clear existing text
		webElement.clear();

		// Send new text
		webElement.sendKeys(text);
	}

	/**
	 * Get visible text of an element.
	 *
	 * @param locator Selenium By locator
	 * @return element text
	 */
	protected String getText(By locator) {
		// Log text retrieval
		LogUtil.debug("Get text from element: " + locator);

		// Return visible text
		return find(locator).getText();
	}

	/**
	 * Check whether an element is displayed.
	 *
	 * @param locator Selenium By locator
	 * @return true if element is displayed, false otherwise
	 */
	protected boolean isDisplayed(By locator) {
		try {
			// Attempt to find element and check visibility
			return find(locator).isDisplayed();
		} catch (Exception e) {
			// Log failure (element not found or not visible)
			LogUtil.warn("Element not displayed: " + locator);
			return false;
		}

	}
}
