package com.example.api.endpoints;

import com.example.api.models.request.UserRequest;
import com.example.api.utils.ApiException;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.function.Supplier;

import static io.restassured.RestAssured.given;

public class UserAPI {

    private static final String USERS_ENDPOINT = "/users";

    public Response createUser(RequestSpecification requestSpecification, UserRequest requestBody) {
        return execute("createUser", () -> given()
                .spec(requestSpecification)
                .body(requestBody)
                .post(USERS_ENDPOINT));
    }

    public Response getUser(RequestSpecification requestSpecification, int userId) {
        return execute("getUser", () -> given()
                .spec(requestSpecification)
                .pathParam("id", userId)
                .get(USERS_ENDPOINT + "/{id}"));
    }

    private Response execute(String operationName, Supplier<Response> requestExecutor) {
        try {
            Response response = requestExecutor.get();
            if (response == null) {
                throw new ApiException("No response received for operation: " + operationName);
            }
            if (response.getStatusCode() >= 500) {
                throw new ApiException("Server error during operation " + operationName + ": " + response.asString());
            }
            return response;
        } catch (ApiException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new ApiException("Failed to execute operation: " + operationName, exception);
        }
    }
}

