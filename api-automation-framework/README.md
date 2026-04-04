# Standalone API Automation Framework

This is a separate Maven project for API automation so API and UI automation can be maintained independently.

## Tech Stack

- Java 17
- Maven
- Rest Assured
- TestNG
- Jackson
- Allure Reporting
- JSON Schema Validation

## Features

- Layered framework structure
- Reusable `BaseTest`
- Environment profiles: `dev`, `qa`, `prod`
- Reusable `RequestSpecification`
- Bearer token handling
- Request and response logging
- POJO-based request and response models
- Positive and negative API test coverage
- JSON schema validation
- API chaining with `JsonPath`
- Deterministic local mock API for `dev`

## Project Structure

```text
api-automation-framework/
├── pom.xml
├── testng.xml
├── src/
│   ├── main/
│   │   └── java/com/example/api/
│   │       ├── auth/
│   │       ├── endpoints/
│   │       ├── models/
│   │       │   ├── request/
│   │       │   └── response/
│   │       ├── payloads/
│   │       └── utils/
│   └── test/
│       ├── java/com/example/api/
│       │   ├── base/
│       │   └── tests/
│       └── resources/
│           ├── schemas/
│           ├── testdata/
│           ├── config-dev.properties
│           ├── config-qa.properties
│           └── config-prod.properties
```

## Run Tests

Default `dev` profile:

```bash
cd /Users/dineshdattasanka/IdeaProjects/SeleniumPractice/api-automation-framework
mvn clean test
```

Run with QA profile:

```bash
cd /Users/dineshdattasanka/IdeaProjects/SeleniumPractice/api-automation-framework
mvn clean test -Pqa
```

Run with Prod profile:

```bash
cd /Users/dineshdattasanka/IdeaProjects/SeleniumPractice/api-automation-framework
mvn clean test -Pprod
```

Generate Allure report:

```bash
cd /Users/dineshdattasanka/IdeaProjects/SeleniumPractice/api-automation-framework
mvn allure:report
```

## Where `baseURI` Is Configured

`ConfigReader` loads the active environment file based on the Maven profile:

- `src/test/resources/config-dev.properties`
- `src/test/resources/config-qa.properties`
- `src/test/resources/config-prod.properties`

`BaseTest` sets:

- `RestAssured.baseURI`
- `RestAssured.basePath`

You can also override any property with a JVM property, for example:

```bash
mvn clean test -DbaseURI=http://localhost:9988
```

