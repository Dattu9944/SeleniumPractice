package com.example.api.auth;

import com.example.api.endpoints.AuthAPI;
import com.example.api.models.request.AuthRequest;
import com.example.api.models.response.AuthResponse;
import com.example.api.payloads.AuthPayloadBuilder;
import com.example.api.utils.ApiException;
import com.example.api.utils.RequestSpecFactory;
import io.restassured.response.Response;

public class AuthTokenProvider {

    private final AuthAPI authAPI;
    private String accessToken;
    private long tokenExpiryTimestamp;

    public AuthTokenProvider(AuthAPI authAPI) {
        this.authAPI = authAPI;
    }

    public synchronized String getAccessToken() {
        if (accessToken != null && System.currentTimeMillis() < tokenExpiryTimestamp) {
            return accessToken;
        }

        AuthRequest authRequest = AuthPayloadBuilder.validCredentials();
        Response response = authAPI.generateToken(RequestSpecFactory.createUnauthenticatedRequestSpecification(), authRequest);

        if (response.getStatusCode() != 200) {
            throw new ApiException("Unable to generate bearer token. Status: " + response.getStatusCode()
                    + ", response: " + response.asString());
        }

        AuthResponse authResponse = response.as(AuthResponse.class);
        accessToken = authResponse.getAccessToken();
        tokenExpiryTimestamp = System.currentTimeMillis() + (authResponse.getExpiresIn() * 1000L) - 5000L;
        return accessToken;
    }
}

