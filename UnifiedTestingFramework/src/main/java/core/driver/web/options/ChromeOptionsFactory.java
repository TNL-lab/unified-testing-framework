package core.driver.web.options;

import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Factory responsible for building ChromeOptions. This class handles browser
 * startup configuration only.
 */
public class ChromeOptionsFactory {
	/**
	 * Creates and returns ChromeOptions with predefined arguments.
	 *
	 * @return configured ChromeOptions instance
	 */
	public static ChromeOptions create() {

		ChromeOptions chromeOptions = new ChromeOptions();

		// Start browser in maximized mode at launch time
		chromeOptions.addArguments("--start-maximized");
		// Additional arguments can be added here

		return chromeOptions;
	}
}
