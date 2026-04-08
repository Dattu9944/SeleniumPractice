package com.example.ui.steps;

import com.example.ui.base.DriverManager;
import com.example.ui.pages.LoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.net.URL;
import java.util.Objects;

public class LoginSteps {

    private LoginPage loginPage;

    @Given("I open the local login page")
    public void iOpenTheLocalLoginPage() {
        URL resource = getClass().getClassLoader().getResource("pages/login.html");
        DriverManager.getDriver().get(Objects.requireNonNull(resource, "Login page resource not found.").toExternalForm());
        loginPage = new LoginPage(DriverManager.getDriver());
    }

    //Do changes on login
    public void newTestMethod(){
        newTestMethod();
    }

    @When("I log in with username {string} and password {string}")
    public void iLogInWithUsernameAndPassword(String username, String password) {
        ensurePageLoaded();
        loginPage.login(username, password);
    }

    @Then("I should see the message {string}")
    public void iShouldSeeTheMessage(String expectedMessage) {
        ensurePageLoaded();
        Assert.assertEquals(loginPage.getMessage(), expectedMessage, "Unexpected login result message.");
    }

    private void ensurePageLoaded() {
        if (loginPage == null) {
            loginPage = new LoginPage(DriverManager.getDriver());
        }
    }
}

