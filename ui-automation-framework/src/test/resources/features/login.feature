Feature: Demo login page
  As a UI automation engineer
  I want readable login scenarios in Gherkin
  So that test cases are easy to understand

  Scenario: Successful login with valid credentials
    Given I open the local login page
    When I log in with username "standard_user" and password "secret_sauce"
    Then I should see the message "Welcome, standard_user!"

  Scenario: Failed login with invalid credentials
    Given I open the local login page
    When I log in with username "wrong_user" and password "wrong_password"
    Then I should see the message "Invalid credentials"

