package core;

import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;

import core.context.WebContextOld;
import core.driver.web.WebDriverManager;
import core.utils.LogUtil;

/**
 * BaseWebTest
 *
 * - Initializes WebDriver
 * - Binds WebDriver to TestContext
 * - Navigates to base URL
 *
 * Used ONLY for WEB tests
 */
public abstract class BaseWebTest extends BaseTest{

	protected WebDriver webDriver;
	
	@BeforeEach
	void setUpWeb() {
        LogUtil.info("Initializing WebDriver");
		// Create WebDriver using WebDriverManager factory
		WebDriver driver = WebDriverManager.createDriver();
        
        // Bind WebDriver to current test context
    	WebContextOld.getContext().setWebDriver(driver);
	}
}
