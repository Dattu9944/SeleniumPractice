package com.example.api.payloads;

import com.example.api.models.request.UserRequest;

import java.util.Map;

public final class UserPayloadBuilder {

    private UserPayloadBuilder() {
    }

    public static UserRequest buildValidUser(Map<String, Object> sourceData) {
        String uniqueSuffix = String.valueOf(System.currentTimeMillis());

        UserRequest userRequest = new UserRequest();
        userRequest.setFirstName(String.valueOf(sourceData.getOrDefault("firstName", "API")));
        userRequest.setLastName(String.valueOf(sourceData.getOrDefault("lastName", "User")));
        userRequest.setEmail(makeUniqueEmail(String.valueOf(sourceData.getOrDefault("email", "api.user@example.com")), uniqueSuffix));
        userRequest.setUsername(String.valueOf(sourceData.getOrDefault("username", "api.user")) + "-" + uniqueSuffix);
        userRequest.setPhone(String.valueOf(sourceData.getOrDefault("phone", "+1-202-555-0101")));
        userRequest.setCompany(String.valueOf(sourceData.getOrDefault("company", "Automation")));
        return userRequest;
    }

    public static UserRequest buildInvalidUserWithMissingFirstName(Map<String, Object> sourceData) {
        UserRequest userRequest = buildValidUser(sourceData);
        userRequest.setFirstName("");
        return userRequest;
    }

    private static String makeUniqueEmail(String email, String uniqueSuffix) {
        int atIndex = email.indexOf('@');
        if (atIndex == -1) {
            return email + "." + uniqueSuffix + "@example.com";
        }
        return email.substring(0, atIndex) + "+" + uniqueSuffix + email.substring(atIndex);
    }
}

