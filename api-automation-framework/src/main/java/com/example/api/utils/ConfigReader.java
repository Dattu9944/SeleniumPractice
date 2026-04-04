package com.example.api.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigReader {

    private static final Properties PROPERTIES = new Properties();

    static {
        String environment = System.getProperty("test.env", "dev").trim().toLowerCase();
        String fileName = "config-" + environment + ".properties";

        try (InputStream inputStream = ConfigReader.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new IllegalStateException("Configuration file not found: " + fileName);
            }
            PROPERTIES.load(inputStream);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load configuration.", exception);
        }
    }

    private ConfigReader() {
    }

    public static String getRequiredProperty(String key) {
        String value = getProperty(key, null);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing required property: " + key);
        }
        return value;
    }

    public static String getProperty(String key, String defaultValue) {
        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue.trim();
        }

        String environmentValue = System.getenv(toEnvironmentVariableName(key));
        if (environmentValue != null && !environmentValue.isBlank()) {
            return environmentValue.trim();
        }

        String fileValue = PROPERTIES.getProperty(key);
        return fileValue == null || fileValue.isBlank() ? defaultValue : fileValue.trim();
    }

    public static boolean getBooleanProperty(String key, boolean defaultValue) {
        return Boolean.parseBoolean(getProperty(key, String.valueOf(defaultValue)));
    }

    private static String toEnvironmentVariableName(String key) {
        return key.replace('.', '_').toUpperCase();
    }
}

