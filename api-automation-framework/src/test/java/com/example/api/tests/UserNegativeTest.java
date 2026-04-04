package com.example.api.tests;

import com.example.api.base.BaseTest;
import com.example.api.models.request.AuthRequest;
import com.example.api.models.request.UserRequest;
import com.example.api.models.response.ErrorResponse;
import com.example.api.payloads.UserPayloadBuilder;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

@Feature("User API Negative Coverage")
public class UserNegativeTest extends BaseTest {

    @Test(description = "Validate that token generation fails with invalid client credentials.")
    @Story("Invalid Authentication")
    @Description("Sends invalid authentication credentials and validates the unauthorized response.")
    public void testGenerateTokenWithInvalidCredentials() {
        AuthRequest invalidRequest = new AuthRequest();
        invalidRequest.setClientId("invalid-client");
        invalidRequest.setClientSecret("invalid-secret");

        Response response = authAPI.generateToken(unauthenticatedRequestSpecification, invalidRequest);
        ErrorResponse errorResponse = response.as(ErrorResponse.class);

        Assert.assertEquals(response.getStatusCode(), 401, "Unexpected status code for invalid credentials.");
        Assert.assertEquals(errorResponse.getError(), "Unauthorized", "Unexpected error type.");
        Assert.assertTrue(errorResponse.getMessage().contains("Invalid API client credentials"), "Unexpected error message.");
        response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/error-response-schema.json"));
    }

    @Test(description = "Validate that creating a user without a bearer token returns unauthorized.")
    @Story("Unauthorized User Create")
    @Description("Attempts to create a user without authorization headers and validates the 401 response.")
    public void testCreateUserWithoutAuthToken() {
        UserRequest request = UserPayloadBuilder.buildValidUser(testData);

        Response response = userAPI.createUser(unauthenticatedRequestSpecification, request);
        ErrorResponse errorResponse = response.as(ErrorResponse.class);

        Assert.assertEquals(response.getStatusCode(), 401, "Unexpected status code for unauthorized create user.");
        Assert.assertEquals(errorResponse.getError(), "Unauthorized", "Unexpected error type.");
        Assert.assertTrue(errorResponse.getMessage().contains("bearer token"), "Unauthorized message should mention bearer token.");
        response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/error-response-schema.json"));
    }

    @Test(description = "Validate that a bad payload returns a proper validation error.")
    @Story("Invalid Payload")
    @Description("Creates a user request with a missing mandatory field and validates the 400 response.")
    public void testCreateUserWithInvalidPayload() {
        UserRequest invalidRequest = UserPayloadBuilder.buildInvalidUserWithMissingFirstName(testData);

        Response response = userAPI.createUser(authenticatedRequestSpecification, invalidRequest);
        ErrorResponse errorResponse = response.as(ErrorResponse.class);

        Assert.assertEquals(response.getStatusCode(), 400, "Unexpected status code for invalid payload.");
        Assert.assertEquals(errorResponse.getError(), "Bad Request", "Unexpected error type.");
        Assert.assertTrue(errorResponse.getMessage().contains("firstName"), "Validation message should mention firstName.");
        response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/error-response-schema.json"));
    }

    @Test(description = "Validate that requesting an unknown user id returns not found.")
    @Story("User Not Found")
    @Description("Fetches a non-existing user id and validates the 404 response and error schema.")
    public void testGetUserWithUnknownId() {
        Response response = userAPI.getUser(authenticatedRequestSpecification, 999999);
        ErrorResponse errorResponse = response.as(ErrorResponse.class);

        Assert.assertEquals(response.getStatusCode(), 404, "Unexpected status code for unknown user.");
        Assert.assertEquals(errorResponse.getError(), "Not Found", "Unexpected error type.");
        Assert.assertTrue(errorResponse.getMessage().contains("User not found"), "Unexpected not found message.");
        response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/error-response-schema.json"));
    }
}

