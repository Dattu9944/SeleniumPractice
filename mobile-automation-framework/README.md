# Standalone Mobile Automation Framework

This is a separate Maven project for mobile automation so API, UI, and Mobile can be managed independently.

## Tech Stack

- Java 17
- Maven
- Appium Java Client
- TestNG
- Allure Reporting

## Features

- Layered framework structure
- Reusable `BaseTest` for Android driver setup and teardown
- Environment profiles: `dev`, `qa`, `prod`
- Dynamic capabilities through properties
- Page Object Model for mobile screens
- Safe default (`run.mobile.tests=false`) to avoid accidental device execution

## Project Structure

```text
mobile-automation-framework/
├── pom.xml
├── testng.xml
├── src/
│   ├── main/
│   │   └── java/com/example/mobile/
│   │       ├── pages/
│   │       └── utils/
│   └── test/
│       ├── java/com/example/mobile/
│       │   ├── base/
│       │   └── tests/
│       └── resources/
│           ├── config-dev.properties
│           ├── config-qa.properties
│           └── config-prod.properties
```

## Run Tests

Default `dev` profile (tests skipped by config):

```bash
cd /Users/dineshdattasanka/IdeaProjects/SeleniumPractice/mobile-automation-framework
mvn clean test
```

Run tests against a running Appium server:

```bash
cd /Users/dineshdattasanka/IdeaProjects/SeleniumPractice/mobile-automation-framework
mvn clean test -Drun.mobile.tests=true -Dappium.server.url=http://127.0.0.1:4723
```

Run with QA profile:

```bash
cd /Users/dineshdattasanka/IdeaProjects/SeleniumPractice/mobile-automation-framework
mvn clean test -Pqa -Drun.mobile.tests=true
```

Generate Allure report:

```bash
cd /Users/dineshdattasanka/IdeaProjects/SeleniumPractice/mobile-automation-framework
mvn allure:report
```

