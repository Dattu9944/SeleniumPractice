package com.example.api.payloads;

import com.example.api.models.request.AuthRequest;
import com.example.api.utils.ConfigReader;

public final class AuthPayloadBuilder {

    private AuthPayloadBuilder() {
    }

    public static AuthRequest validCredentials() {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setClientId(ConfigReader.getRequiredProperty("api.auth.clientId"));
        authRequest.setClientSecret(ConfigReader.getRequiredProperty("api.auth.clientSecret"));
        return authRequest;
    }
}

