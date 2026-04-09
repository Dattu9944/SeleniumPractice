# Mobile Automation Framework Guide

Project: `mobile-automation-framework`

## Purpose

This project is a standalone Appium mobile automation framework using:
- Java 17
- Maven
- Appium Java Client
- TestNG
- Allure

It is designed for Android native app validation with Page Object Model.

## Layered Structure

- `src/main/java/com/example/mobile/pages` -> page objects (`LoginPage`)
- `src/main/java/com/example/mobile/utils` -> config utility (`ConfigReader`)
- `src/test/java/com/example/mobile/base` -> Android driver lifecycle (`BaseTest`)
- `src/test/java/com/example/mobile/tests` -> TestNG tests
- `src/test/resources` -> profile configs (`dev`, `qa`, `prod`)

## Driver Setup

Mobile driver setup is implemented in:
- `src/test/java/com/example/mobile/base/BaseTest.java`

In `setUp()`:
- Reads environment from config profile
- Creates `UiAutomator2Options`
- Starts `AndroidDriver` using `appium.server.url`
- Applies implicit wait timeout

`tearDown()` calls `driver.quit()`.

## Profiles and Safety

Profile files:
- `src/test/resources/config-dev.properties`
- `src/test/resources/config-qa.properties`
- `src/test/resources/config-prod.properties`

By default:
- `run.mobile.tests=false`

This prevents accidental execution without emulator/device and Appium server.

## Run Commands

```bash
cd /Users/dineshdattasanka/IdeaProjects/SeleniumPractice/mobile-automation-framework
mvn clean test
```

Run real mobile execution:

```bash
cd /Users/dineshdattasanka/IdeaProjects/SeleniumPractice/mobile-automation-framework
mvn clean test -Drun.mobile.tests=true -Dappium.server.url=http://127.0.0.1:4723
```

## Reporting

```bash
cd /Users/dineshdattasanka/IdeaProjects/SeleniumPractice/mobile-automation-framework
mvn allure:report
```

