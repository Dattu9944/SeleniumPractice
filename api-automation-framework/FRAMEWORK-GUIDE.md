# API Automation Framework Guide

Project: `api-automation-framework`

## Purpose

This project is a standalone API automation framework using:
- Java 17
- Maven
- Rest Assured
- TestNG
- Allure

It is designed for API functional validation, negative testing, auth/token validation, schema validation, and API chaining.

## Layered Structure

- `src/main/java/com/example/api/endpoints` -> API client classes (`AuthAPI`, `UserAPI`)
- `src/main/java/com/example/api/payloads` -> reusable payload builders
- `src/main/java/com/example/api/models/request` -> request POJOs
- `src/main/java/com/example/api/models/response` -> response/error POJOs
- `src/main/java/com/example/api/utils` -> config/json/spec/mock server utilities
- `src/main/java/com/example/api/auth` -> token provider
- `src/test/java/com/example/api/base` -> shared test setup
- `src/test/java/com/example/api/tests` -> positive + negative TestNG suites
- `src/test/resources` -> config profiles, schemas, test data

## Configuration and Profiles

Profiles are controlled via Maven:
- `dev`
- `qa`
- `prod`

Files:
- `src/test/resources/config-dev.properties`
- `src/test/resources/config-qa.properties`
- `src/test/resources/config-prod.properties`

`BaseTest` configures:
- `RestAssured.baseURI`
- `RestAssured.basePath`

## Test Coverage

- Positive flow: create user -> extract ID -> get user
- Negative flow: invalid token/auth, invalid payload, unknown user
- Schema checks:
  - `src/test/resources/schemas/user-response-schema.json`
  - `src/test/resources/schemas/error-response-schema.json`

## Run Commands

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

## Reporting

```bash
cd /Users/dineshdattasanka/IdeaProjects/SeleniumPractice/api-automation-framework
mvn allure:report
```

