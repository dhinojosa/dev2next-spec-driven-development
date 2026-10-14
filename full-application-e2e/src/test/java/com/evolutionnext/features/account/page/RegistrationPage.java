package com.evolutionnext.features.account.page;

import com.evolutionnext.e2e.support.BrowserSupport;
import org.openqa.selenium.By;

public final class RegistrationPage {
    private final BrowserSupport browser;

    public RegistrationPage(BrowserSupport browser) {
        this.browser = browser;
    }

    public RegistrationPage open() {
        browser.open("/account/register");
        browser.waitForElement(By.name("userName"));
        return this;
    }

    public DashboardPage register(String userName, String password) {
        browser.replaceInput(By.name("userName"), userName);
        browser.replaceInput(By.name("password"), password);
        browser.submitForm(By.cssSelector("form[action='/account/register']"));
        return new DashboardPage(browser).waitUntilVisible();
    }
}
