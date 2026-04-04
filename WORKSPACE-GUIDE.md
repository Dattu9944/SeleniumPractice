# Automation Framework Guide (API + UI)

This workspace contains **two separate automation projects**:

- `api-automation-framework` for API testing
- `ui-automation-framework` for Selenium UI testing

Use this guide as the quick onboarding document for both.

---

## 1) Workspace Layout

```text
SeleniumPractice/
├── api-automation-framework/
└── ui-automation-framework/
```

Both are standalone Maven projects with their own `pom.xml`, `testng.xml`, source code, and resources.

---

## 2) API Project Overview

Project: `api-automation-framework`

### Purpose
- API functional validation with Rest Assured + TestNG
- Positive and negative coverage
- API chaining (create user -> get user)
- Schema validation and auth token handling

### Architecture
- `base` -> test setup (`BaseTest`)
- `utils` -> config/json/spec/mock server
- `endpoints` -> API clients (`AuthAPI`, `UserAPI`)
- `payloads` -> request builders
- `models` -> POJO request/response classes
- `tests` -> TestNG test classes
- `resources` -> env configs, schemas, test data

### Key Files
- `api-automation-framework/src/test/java/com/example/api/base/BaseTest.java`
- `api-automation-framework/src/main/java/com/example/api/utils/ConfigReader.java`
- `api-automation-framework/src/main/java/com/example/api/endpoints/UserAPI.java`
- `api-automation-framework/src/test/java/com/example/api/tests/UserPositiveTest.java`
- `api-automation-framework/src/test/java/com/example/api/tests/UserNegativeTest.java`

### Config
Profile-based config files:
- `api-automation-framework/src/test/resources/config-dev.properties`
- `api-automation-framework/src/test/resources/config-qa.properties`
- `api-automation-framework/src/test/resources/config-prod.properties`

`BaseTest` sets:
- `RestAssured.baseURI`
- `RestAssured.basePath`

### Run API Tests
```bash
cd /Users/dineshdattasanka/IdeaProjects/SeleniumPractice/api-automation-framework
mvn clean test
```

```bash
cd /Users/dineshdattasanka/IdeaProjects/SeleniumPractice/api-automation-framework
mvn clean test -Pqa
```

```bash
cd /Users/dineshdattasanka/IdeaProjects/SeleniumPractice/api-automation-framework
mvn clean test -Pprod
```

---

## 3) UI Project Overview

Project: `ui-automation-framework`

### Purpose
- UI functional validation with Selenium + TestNG
- POM-based page classes
- Stable local login page tests by default
- Optional external LinkedIn smoke test

### Architecture
- `pages` -> page objects (`LoginPage`, `LinkedInPage`)
- `utils` -> config utility (`ConfigReader`)
- `base` -> WebDriver setup/teardown (`BaseTest`)
- `tests` -> TestNG test classes
- `resources` -> env configs + local HTML test page

### Key Files
- `ui-automation-framework/src/test/java/com/example/ui/base/BaseTest.java`
- `ui-automation-framework/src/main/java/com/example/ui/pages/LoginPage.java`
- `ui-automation-framework/src/main/java/com/example/ui/pages/LinkedInPage.java`
- `ui-automation-framework/src/test/java/com/example/ui/tests/LoginTest.java`
- `ui-automation-framework/src/test/java/com/example/ui/tests/LinkedInTest.java`

### Config
Profile-based config files:
- `ui-automation-framework/src/test/resources/config-dev.properties`
- `ui-automation-framework/src/test/resources/config-qa.properties`
- `ui-automation-framework/src/test/resources/config-prod.properties`

Important properties:
- `browser`
- `headless`
- `implicit.wait.seconds`
- `page.load.timeout.seconds`
- `linkedin.url`
- `linkedin.login.url`
- `run.linkedin.tests`

### Run UI Tests
```bash
cd /Users/dineshdattasanka/IdeaProjects/SeleniumPractice/ui-automation-framework
mvn clean test
```

Run LinkedIn test explicitly:
```bash
cd /Users/dineshdattasanka/IdeaProjects/SeleniumPractice/ui-automation-framework
mvn clean test -Dtest=LinkedInTest -Drun.linkedin.tests=true -Dheadless=true
```

---

## 4) Reporting

Both projects support TestNG/Surefire output in `target/surefire-reports`.

Allure plugins are configured in each project `pom.xml`.

Generate report from project directory:
```bash
mvn allure:report
```

---

## 5) Which Project Should You Use?

Use `api-automation-framework` when:
- validating endpoints, status codes, response models, schemas
- testing auth/token behavior
- covering positive + negative API scenarios

Use `ui-automation-framework` when:
- validating browser user flows
- checking page interactions and UI behavior
- running smoke checks on external pages (LinkedIn)

---

## 6) Recommended Daily Workflow

1. Start with API checks for service stability.
2. Run UI regression/smoke for user journey validation.
3. Keep API and UI commits isolated by project folder.
4. Use profile flags (`dev`, `qa`, `prod`) for environment switching.

