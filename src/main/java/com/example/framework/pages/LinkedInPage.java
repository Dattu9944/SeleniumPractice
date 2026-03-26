package com.example.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LinkedInPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By homeMarker = By.cssSelector("a[href*='linkedin.com/login'], a.nav__button-secondary");
    private final By acceptCookieButton = By.cssSelector("button[action-type='ACCEPT'], button[id*='accept'], button[aria-label*='Accept']");
    private final By emailInput = By.id("username");
    private final By passwordInput = By.id("password");

    public LinkedInPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void openHome() {
        driver.get("https://www.linkedin.com/");
    }

    public boolean isHomeLoaded() {
        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.urlContains("linkedin.com"),
                    ExpectedConditions.titleContains("LinkedIn")
            ));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void acceptCookiesIfPresent() {
        try {
            WebElement cookieButton = new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.elementToBeClickable(acceptCookieButton));
            cookieButton.click();
        } catch (TimeoutException | NoSuchElementException ignored) {
            // Cookie prompt is not always shown, so this step is optional.
        }
    }

    public void openSignInPage() {
        driver.get("https://www.linkedin.com/login");
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput));
    }

    public void enterEmail(String email) {
        WebElement emailElement = wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput));
        emailElement.clear();
        emailElement.sendKeys(email);
    }

    public void enterPassword(String password) {
        WebElement passwordElement = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInput));
        passwordElement.clear();
        passwordElement.sendKeys(password);
    }

    public boolean isLoginFormVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput));
            wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInput));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }
}

