# Selenium Java Automation Framework (POM)

This project is a starter Selenium automation framework using:
- Java + Maven
- TestNG test runner
- WebDriverManager for driver management
- Page Object Model (POM)

## Project Structure

```text
SeleniumPractice/
├── pom.xml
├── testng.xml
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/example/framework/pages/
│   │           ├── LinkedInPage.java
│   │           └── LoginPage.java
│   └── test/
│       ├── java/
│       │   └── com/example/framework/tests/
│       │       ├── BaseTest.java
│       │       ├── LinkedInTest.java
│       │       └── LoginTest.java
│       └── resources/
│           └── pages/
│               └── login.html
```

## Run Tests

```bash
mvn clean test
```

## Optional Runtime Flags

Run with headed Chrome:

```bash
mvn clean test -Dheadless=false
```

Run only the LinkedIn smoke test:

```bash
mvn -Dtest=LinkedInTest test -Dheadless=false
```

Optionally provide LinkedIn credentials as JVM properties (test fills fields but does not submit):

```bash
mvn -Dtest=LinkedInTest test -Dheadless=false -Dlinkedin.email="your_email" -Dlinkedin.password="your_password"
```

