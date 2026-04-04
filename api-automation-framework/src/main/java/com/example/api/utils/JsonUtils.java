package com.example.api.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public final class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().findAndRegisterModules();

    private JsonUtils() {
    }

    public static Map<String, Object> readJsonAsMap(String resourcePath) {
        return readJsonFromResource(resourcePath, new TypeReference<>() {
        });
    }

    public static <T> T readJsonFromResource(String resourcePath, TypeReference<T> typeReference) {
        try (InputStream inputStream = JsonUtils.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new ApiException("JSON resource not found: " + resourcePath);
            }
            return OBJECT_MAPPER.readValue(inputStream, typeReference);
        } catch (IOException exception) {
            throw new ApiException("Unable to read JSON resource: " + resourcePath, exception);
        }
    }

    public static <T> T fromJson(String json, Class<T> targetType) {
        try {
            return OBJECT_MAPPER.readValue(json, targetType);
        } catch (IOException exception) {
            throw new ApiException("Unable to deserialize JSON into " + targetType.getSimpleName(), exception);
        }
    }

    public static String toJson(Object value) {
        try {
            return OBJECT_MAPPER.writeValueAsString(value);
        } catch (IOException exception) {
            throw new ApiException("Unable to serialize object to JSON.", exception);
        }
    }
}

