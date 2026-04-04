@linkedin
Feature: LinkedIn login page smoke
  As a UI automation engineer
  I want a readable LinkedIn smoke scenario
  So that I can quickly understand the browser flow

  Scenario: Open the LinkedIn login page
    Given LinkedIn smoke tests are enabled
    When I open the LinkedIn login page
    Then the LinkedIn sign in form should be visible

