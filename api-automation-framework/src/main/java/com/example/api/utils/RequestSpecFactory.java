package com.example.api.utils;

import com.example.api.auth.AuthTokenProvider;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public final class RequestSpecFactory {

    private RequestSpecFactory() {
    }

    public static RequestSpecification createUnauthenticatedRequestSpecification() {
        return createBaseSpecification();
    }

    public static RequestSpecification createAuthenticatedRequestSpecification(AuthTokenProvider tokenProvider) {
        String tokenHeader = ConfigReader.getProperty("api.auth.tokenHeader", "Authorization");
        String tokenPrefix = ConfigReader.getProperty("api.auth.tokenPrefix", "Bearer");

        return new RequestSpecBuilder()
                .addRequestSpecification(createBaseSpecification())
                .addHeader(tokenHeader, tokenPrefix + " " + tokenProvider.getAccessToken())
                .build();
    }

    private static RequestSpecification createBaseSpecification() {
        return new RequestSpecBuilder()
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .addHeader("X-Test-Client", "standalone-api-automation-framework")
                .addFilter(new AllureRestAssured())
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();
    }
}

