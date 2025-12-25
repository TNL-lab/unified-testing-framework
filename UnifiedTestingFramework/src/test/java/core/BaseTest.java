package core;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;

import core.config.EnvironmentConfig;
import core.context.WebContextOld;
import core.driver.web.WebDriverManager;
import core.enums.PlatformType;
import core.utils.DebugUtil;
import core.utils.LogUtil;

/**
 * BaseTest
 *
 * - Parent class for all test types
 * - Contains ONLY common lifecycle logic
 * - Does NOT know about Web / API / Mobile
 */
public abstract class BaseTest {

    /**
     * Setup method executed before each test.
     * Initializes WebDriver and binds it to TestContext.
     */
	@BeforeEach
	void setUp() {
		LogUtil.info("===== START TEST =====");
	}

    /**
     * Teardown method executed after each test method.
     * Closes and cleans up driver resources.
     */

	@AfterEach
	void tearDown() {
		// Optional debug pause (for local debugging only)
		if (EnvironmentConfig.isDebugMode() && EnvironmentConfig.isDebugPauseEnabled()) {
			DebugUtil.pause(EnvironmentConfig.debugPauseSeconds());
		}
		
        // Clean up TestContext and quit WebDriver
        WebContextOld.clear();

        LogUtil.info("===== END TEST =====");
	}
}
