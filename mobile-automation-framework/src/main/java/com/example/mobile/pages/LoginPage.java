package com.example.mobile.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    private final AndroidDriver driver;
    private final WebDriverWait wait;

    private final By usernameField = AppiumBy.accessibilityId("username");
    private final By passwordField = AppiumBy.accessibilityId("password");
    private final By loginButton = AppiumBy.accessibilityId("login_button");
    private final By statusMessage = AppiumBy.accessibilityId("status_message");

    public LoginPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void login(String username, String password) {
        type(usernameField, username);
        type(passwordField, password);
        tap(loginButton);
    }

    public String getStatusMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(statusMessage)).getText();
    }

    private void type(By locator, String value) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(value);
    }

    private void tap(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }
}

