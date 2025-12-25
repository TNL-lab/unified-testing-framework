package core.driver.web;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import core.config.EnvironmentConfig;
import core.driver.web.options.ChromeOptionsFactory;
import core.wait.WaitApplier;
import web.enums.BrowserType;

/**
 * Factory class for creating WebDriver instances.
 */
public final class WebDriverManager {

	private WebDriverManager() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * Create and fully configure WebDriver for current thread.
	 * 
	 * @return initialized WebDriver
	 */
	public static WebDriver createDriver() {
		// Determine browser type from configuration
		BrowserType browserType = EnvironmentConfig.getBrowser();

		// Create WebDriver instance
		WebDriver driver = createBrowser(browserType);

		// Apply global wait & timeout configuration
		WaitApplier.apply(driver);
		
    	//Navigate to base URL 
        driver.get(EnvironmentConfig.getWebUrl());

		return driver;
	}

	/**
	 * Create WebDriver by browser type
	 *
	 * @param browser BrowserType enum
	 * @return raw WebDriver instance
	 */
	private static WebDriver createBrowser(BrowserType browserType) {
		return switch (browserType) {
		case CHROME -> new ChromeDriver(ChromeOptionsFactory.create());

		case FIREFOX -> new FirefoxDriver();

		case EDGE -> new EdgeDriver();

		default -> throw new IllegalArgumentException("Unsupported browser:" + browserType);
		};
	}

}
