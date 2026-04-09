package com.example.mobile.base;

import com.example.mobile.utils.ConfigReader;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class BaseTest {

    protected AndroidDriver driver;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        boolean runMobileTests = Boolean.parseBoolean(ConfigReader.get("run.mobile.tests", "false"));
        if (!runMobileTests) {
            throw new SkipException("Mobile tests are disabled. Set -Drun.mobile.tests=true to execute.");
        }

        UiAutomator2Options options = new UiAutomator2Options();
        options.setPlatformName(ConfigReader.get("platformName"));
        options.setDeviceName(ConfigReader.get("deviceName"));
        options.setAutomationName(ConfigReader.get("automationName", "UiAutomator2"));

        String appPath = ConfigReader.get("app.path", "").trim();
        if (!appPath.isEmpty()) {
            options.setApp(appPath);
        } else {
            options.setAppPackage(ConfigReader.get("app.package"));
            options.setAppActivity(ConfigReader.get("app.activity"));
        }

        String appiumServerUrl = ConfigReader.get("appium.server.url");
        try {
            driver = new AndroidDriver(new URL(appiumServerUrl), options);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid appium.server.url: " + appiumServerUrl, e);
        }

        long implicitWaitSeconds = Long.parseLong(ConfigReader.get("implicit.wait.seconds", "10"));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWaitSeconds));
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}

