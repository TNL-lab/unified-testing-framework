package core;

import org.junit.jupiter.api.BeforeEach;

import core.utils.LogUtil;

/**
 * BaseApiTest
 *
 * Used ONLY for Api tests
 */
public abstract class BaseApiTest extends BaseTest{
	
	@BeforeEach
	void setUpApi() {
        LogUtil.info("Initializing Api Client");
        //
	}
}
