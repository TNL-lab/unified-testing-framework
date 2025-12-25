package web.components;

import org.openqa.selenium.By;

import web.pages.BasePage;

/**
 * HeaderComponent - Reusable UI component
 */
public class HeaderComponent extends BasePage {
	private final By logoutBtn = By.id("logout");
	private final By userName = By.cssSelector(".user-name");

	public HeaderComponent() {
		super();
	}

	public void logout() {
		click(logoutBtn);
	}

	public String getUsename() {
		return getText(userName);
	}
}
