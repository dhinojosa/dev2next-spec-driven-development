package com.evolutionnext.features.account.page;

import com.evolutionnext.e2e.support.BrowserSupport;
import org.openqa.selenium.By;

public final class LoginPage {
    private final BrowserSupport browser;

    public LoginPage(BrowserSupport browser) {
        this.browser = browser;
    }

    public LoginPage open() {
        browser.open("/account/login");
        browser.waitForElement(By.name("userName"));
        return this;
    }

    public DashboardPage logIn(String userName, String password) {
        browser.replaceInput(By.name("userName"), userName);
        browser.replaceInput(By.name("password"), password);
        browser.submitForm(By.cssSelector("form[action='/account/login']"));
        return new DashboardPage(browser).waitUntilVisible();
    }
}
