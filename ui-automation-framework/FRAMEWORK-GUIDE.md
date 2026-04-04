# UI Automation Framework Guide

Project: `ui-automation-framework`

## Purpose

This project is a standalone Selenium UI automation framework using:
- Java 17
- Maven
- Selenium WebDriver
- TestNG
- WebDriverManager
- Allure

It is designed for browser-based UI functional testing with Page Object Model.

## Layered Structure

- `src/main/java/com/example/ui/pages` -> page objects (`LoginPage`, `LinkedInPage`)
- `src/main/java/com/example/ui/utils` -> config utility
- `src/test/java/com/example/ui/base` -> shared WebDriver lifecycle
- `src/test/java/com/example/ui/tests` -> TestNG tests
- `src/test/resources` -> profile configs + local UI test page

## ChromeDriver Setup (Yes, configured)

ChromeDriver setup is already implemented in:
- `src/test/java/com/example/ui/base/BaseTest.java`

In `setUp()`:
- `WebDriverManager.chromedriver().setup()` downloads/resolves the correct ChromeDriver
- `driver = new ChromeDriver(options)` launches Chrome browser
- `headless` mode is controlled by config (`headless=true/false`)

So yes, UI browser launch is configured.

## Configuration and Profiles

Profile files:
- `src/test/resources/config-dev.properties`
- `src/test/resources/config-qa.properties`
- `src/test/resources/config-prod.properties`

Important properties:
- `browser=chrome`
- `headless=true`
- `implicit.wait.seconds`
- `page.load.timeout.seconds`
- `linkedin.url`
- `linkedin.login.url`
- `run.linkedin.tests`

## Test Coverage

- Local deterministic login tests (`LoginTest`) are part of default suite
- Optional LinkedIn smoke test (`LinkedInTest`) can be triggered manually

## Run Commands

```bash
cd /Users/dineshdattasanka/IdeaProjects/SeleniumPractice/ui-automation-framework
mvn clean test
```

Run LinkedIn smoke explicitly:

```bash
cd /Users/dineshdattasanka/IdeaProjects/SeleniumPractice/ui-automation-framework
mvn clean test -Dtest=LinkedInTest -Drun.linkedin.tests=true -Dheadless=true
```

## Reporting

```bash
cd /Users/dineshdattasanka/IdeaProjects/SeleniumPractice/ui-automation-framework
mvn allure:report
```

