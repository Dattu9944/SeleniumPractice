package com.example.ui.steps;

import com.example.ui.base.BaseTest;
import io.cucumber.java.After;
import io.cucumber.java.Before;

public class UiHooks extends BaseTest {

    @Before
    public void beforeScenario() {
        initializeDriver();
    }

    @After
    public void afterScenario() {
        quitDriver();
    }
}

