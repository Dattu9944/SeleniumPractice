package com.example.api.base;

import com.example.api.auth.AuthTokenProvider;
import com.example.api.endpoints.AuthAPI;
import com.example.api.endpoints.UserAPI;
import com.example.api.utils.ConfigReader;
import com.example.api.utils.JsonUtils;
import com.example.api.utils.MockApiServer;
import com.example.api.utils.RequestSpecFactory;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.util.Map;

public class BaseTest {

    protected AuthAPI authAPI;
    protected UserAPI userAPI;
    protected AuthTokenProvider authTokenProvider;
    protected RequestSpecification authenticatedRequestSpecification;
    protected RequestSpecification unauthenticatedRequestSpecification;
    protected Map<String, Object> testData;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        MockApiServer.startIfRequired();

        RestAssured.baseURI = ConfigReader.getRequiredProperty("baseURI");
        RestAssured.basePath = ConfigReader.getProperty("basePath", "");

        authAPI = new AuthAPI();
        userAPI = new UserAPI();
        authTokenProvider = new AuthTokenProvider(authAPI);
        unauthenticatedRequestSpecification = RequestSpecFactory.createUnauthenticatedRequestSpecification();
        authenticatedRequestSpecification = ConfigReader.getBooleanProperty("api.auth.enabled", true)
                ? RequestSpecFactory.createAuthenticatedRequestSpecification(authTokenProvider)
                : RequestSpecFactory.createUnauthenticatedRequestSpecification();
        testData = JsonUtils.readJsonAsMap("testdata/user-data.json");
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        MockApiServer.stopIfRunning();
    }
}

