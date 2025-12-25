package web.pages.login;

import org.openqa.selenium.By;

import core.utils.LogUtil;
import web.pages.BasePage;

/**
 * LoginPage - Encapsulates login page behavior 
 * - Hybrid approach: locators + logic together (small page)
 */
public class LoginPage extends BasePage {

	// Login locator
	private final By usernameInput = By.name("username");
	private final By paswordInput = By.name("password");
	private final By loginBtn = By.cssSelector("button[type='submit'].orangehrm-login-button");

    public LoginPage() {
        super();
    }

	/**
	 * Perform login action
	 */
	public void login(String username, String password) {
		LogUtil.info("Performing login");
		type(usernameInput, username);
		type(paswordInput, password);
		click(loginBtn);
		
	}
}
