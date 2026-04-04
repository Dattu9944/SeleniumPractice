package com.example.api.endpoints;

import com.example.api.models.request.AuthRequest;
import com.example.api.utils.ApiException;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.function.Supplier;

import static io.restassured.RestAssured.given;

public class AuthAPI {

    private static final String TOKEN_ENDPOINT = "/auth/token";

    public Response generateToken(RequestSpecification requestSpecification, AuthRequest requestBody) {
        return execute("generateToken", () -> given()
                .spec(requestSpecification)
                .body(requestBody)
                .post(TOKEN_ENDPOINT));
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

