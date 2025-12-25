package core;

import org.junit.jupiter.api.BeforeEach;

import core.utils.LogUtil;

/**
 * BaseMobileTest
 *
 * Used ONLY for Mobile tests
 */
public abstract class BaseMobileTest extends BaseTest{
	
	@BeforeEach
	void setUpMobile() {
        LogUtil.info("Initializing AppiumDriver");
        //
	}
}
