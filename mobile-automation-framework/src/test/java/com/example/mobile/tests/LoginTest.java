package com.example.mobile.tests;

import com.example.mobile.base.BaseTest;
import com.example.mobile.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(description = "Validate native app login and status message")
    public void testLoginFlow() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("demo_user", "demo_password");

        String message = loginPage.getStatusMessage();
        Assert.assertNotNull(message, "Status message should not be null");
        Assert.assertFalse(message.isBlank(), "Status message should not be empty");
    }
}

