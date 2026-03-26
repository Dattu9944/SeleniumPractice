package com.example.framework.tests;

import com.example.framework.pages.LinkedInPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LinkedInTest extends BaseTest {

    @Test
    public void shouldOpenLinkedInAndLoadSignInForm() {
        LinkedInPage linkedInPage = new LinkedInPage(driver);

        linkedInPage.openHome();
        Assert.assertTrue(linkedInPage.isHomeLoaded(), "LinkedIn home page did not load.");

        linkedInPage.acceptCookiesIfPresent();
        linkedInPage.openSignInPage();
        Assert.assertTrue(linkedInPage.isLoginFormVisible(), "LinkedIn sign-in form is not visible.");

        String email = System.getProperty("linkedin.email", "").trim();
        String password = System.getProperty("linkedin.password", "").trim();

        if (!email.isEmpty() && !password.isEmpty()) {
            linkedInPage.enterEmail(email);
            linkedInPage.enterPassword(password);
        }
    }
}

