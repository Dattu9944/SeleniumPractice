package com.example.ui.steps;

import com.example.ui.base.DriverManager;
import com.example.ui.pages.LinkedInPage;
import com.example.ui.utils.ConfigReader;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import org.testng.SkipException;

public class LinkedInSteps {

    private LinkedInPage linkedInPage;

    @Given("LinkedIn smoke tests are enabled")
    public void linkedInSmokeTestsAreEnabled() {
        if (!ConfigReader.getBooleanProperty("run.linkedin.tests", false)) {
            throw new SkipException("LinkedIn smoke scenario is disabled. Run with -Drun.linkedin.tests=true and -Dcucumber.filter.tags=@linkedin.");
        }
        linkedInPage = new LinkedInPage(DriverManager.getDriver());
    }

    @When("I open the LinkedIn login page")
    public void iOpenTheLinkedInLoginPage() {
        ensurePageLoaded();
        linkedInPage.openHome();
        Assert.assertTrue(linkedInPage.isHomeLoaded(), "LinkedIn home page did not load.");
        linkedInPage.acceptCookiesIfPresent();
        linkedInPage.openSignInPage();
    }

    @Then("the LinkedIn sign in form should be visible")
    public void theLinkedInSignInFormShouldBeVisible() {
        ensurePageLoaded();
        Assert.assertTrue(linkedInPage.isLoginFormVisible(), "LinkedIn sign-in form is not visible.");
    }

    private void ensurePageLoaded() {
        if (linkedInPage == null) {
            linkedInPage = new LinkedInPage(DriverManager.getDriver());
        }
    }
}

