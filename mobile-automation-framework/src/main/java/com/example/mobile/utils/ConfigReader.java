package com.example.mobile.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public final class ConfigReader {

    private static final Properties PROPERTIES = new Properties();

    static {
        String env = System.getProperty("test.env", "dev");
        String fileName = "config-" + env + ".properties";
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                throw new IllegalStateException("Config file not found: " + fileName);
            }
            PROPERTIES.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load configuration.", e);
        }
    }

    private ConfigReader() {
    }

    public static String get(String key) {
        String overridden = System.getProperty(key);
        if (overridden != null && !overridden.isBlank()) {
            return overridden;
        }
        String value = PROPERTIES.getProperty(key);
        if (value == null) {
            throw new IllegalArgumentException("Missing config key: " + key);
        }
        return value;
    }

    public static String get(String key, String defaultValue) {
        String overridden = System.getProperty(key);
        if (overridden != null && !overridden.isBlank()) {
            return overridden;
        }
        return Objects.requireNonNullElse(PROPERTIES.getProperty(key), defaultValue);
    }
}

