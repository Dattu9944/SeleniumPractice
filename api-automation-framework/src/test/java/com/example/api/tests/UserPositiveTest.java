package com.example.api.tests;

import com.example.api.base.BaseTest;
import com.example.api.models.request.UserRequest;
import com.example.api.models.response.UserResponse;
import com.example.api.payloads.UserPayloadBuilder;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

@Feature("User API")
public class UserPositiveTest extends BaseTest {

    private int createdUserId;
    private UserRequest createdUserPayload;

    @Test(priority = 1, description = "Create a user with a valid authenticated request and validate the response schema.")
    @Story("Create User")
    @Description("Creates a user, validates status code, body content, schema, and captures the generated id using JsonPath.")
    public void testCreateUser() {
        createdUserPayload = UserPayloadBuilder.buildValidUser(testData);

        Response createUserResponse = userAPI.createUser(authenticatedRequestSpecification, createdUserPayload);
        JsonPath responseJson = createUserResponse.jsonPath();
        createdUserId = responseJson.getInt("id");
        UserResponse responseBody = createUserResponse.as(UserResponse.class);

        Assert.assertEquals(createUserResponse.getStatusCode(), 201, "Unexpected create user status code.");
        Assert.assertTrue(createdUserId > 0, "Created user id should be greater than zero.");
        Assert.assertNotNull(responseBody.getCreatedAt(), "createdAt should not be null.");
        Assert.assertEquals(responseBody.getFirstName(), createdUserPayload.getFirstName(), "First name mismatch.");
        Assert.assertEquals(responseBody.getLastName(), createdUserPayload.getLastName(), "Last name mismatch.");
        Assert.assertEquals(responseBody.getEmail(), createdUserPayload.getEmail(), "Email mismatch.");
        createUserResponse.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/user-response-schema.json"));
    }

    @Test(priority = 2, dependsOnMethods = "testCreateUser", description = "Fetch a user using the id from the create user response to validate API chaining.")
    @Story("Get User")
    @Description("Uses the created user id from the previous API call and validates the fetched user response and schema.")
    public void testGetUser() {
        Response getUserResponse = userAPI.getUser(authenticatedRequestSpecification, createdUserId);
        UserResponse responseBody = getUserResponse.as(UserResponse.class);

        Assert.assertEquals(getUserResponse.getStatusCode(), 200, "Unexpected get user status code.");
        Assert.assertEquals(responseBody.getId(), createdUserId, "Fetched user id mismatch.");
        Assert.assertEquals(responseBody.getUsername(), createdUserPayload.getUsername(), "Username mismatch.");
        Assert.assertEquals(responseBody.getCompany(), createdUserPayload.getCompany(), "Company mismatch.");
        Assert.assertNotNull(responseBody.getPhone(), "Phone should not be null.");
        getUserResponse.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/user-response-schema.json"));
    }
}

