package web;

import org.junit.jupiter.api.Test;

import core.BaseTest;
import core.BaseWebTest;
import web.pages.login.LoginPage;

public class LoginTest extends BaseWebTest{
	@Test
	void login_successfully() {
		LoginPage loginPage = new LoginPage();
		loginPage.login("admin", "123456");
	}
}
