package com.example.framework.tests;

import com.example.framework.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test
    public void loginWithValidCredentialsShouldShowWelcomeMessage() {
        driver.get(getTestResourceUrl("pages/login.html"));

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        Assert.assertEquals(loginPage.getMessage(), "Welcome, standard_user!");
    }

    @Test
    public void loginWithInvalidCredentialsShouldShowErrorMessage() {
        driver.get(getTestResourceUrl("pages/login.html"));

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("wrong_user", "wrong_password");

        Assert.assertEquals(loginPage.getMessage(), "Invalid credentials");
    }
}

