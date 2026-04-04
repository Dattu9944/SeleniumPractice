package com.example.api.utils;

import com.example.api.models.request.AuthRequest;
import com.example.api.models.request.UserRequest;
import com.example.api.models.response.AuthResponse;
import com.example.api.models.response.ErrorResponse;
import com.example.api.models.response.UserResponse;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public final class MockApiServer {

    private static final String MOCK_ACCESS_TOKEN = "mock-access-token";
    private static final Object LOCK = new Object();
    private static final AtomicInteger USER_ID_SEQUENCE = new AtomicInteger(2000);
    private static final ConcurrentMap<Integer, UserResponse> USERS = new ConcurrentHashMap<>();

    private static HttpServer server;

    private MockApiServer() {
    }

    public static void startIfRequired() {
        if (!ConfigReader.getBooleanProperty("mock.server.enabled", true)) {
            return;
        }

        synchronized (LOCK) {
            if (server != null) {
                return;
            }

            try {
                URI baseUri = URI.create(ConfigReader.getRequiredProperty("baseURI"));
                int port = baseUri.getPort() == -1 ? 80 : baseUri.getPort();
                String host = baseUri.getHost() == null ? "localhost" : baseUri.getHost();
                String basePath = normalizePath(ConfigReader.getProperty("basePath", ""));

                server = HttpServer.create(new InetSocketAddress(host, port), 0);
                server.createContext(basePath + "/auth/token", new AuthHandler(basePath + "/auth/token"));
                server.createContext(basePath + "/users", new UserHandler(basePath + "/users"));
                server.setExecutor(Executors.newCachedThreadPool());
                server.start();
            } catch (IOException exception) {
                throw new ApiException("Unable to start mock API server.", exception);
            }
        }
    }

    public static void stopIfRunning() {
        synchronized (LOCK) {
            if (server != null) {
                server.stop(0);
                server = null;
                USERS.clear();
                USER_ID_SEQUENCE.set(2000);
            }
        }
    }

    private static String normalizePath(String path) {
        String normalized = Objects.requireNonNullElse(path, "").trim();
        if (normalized.isEmpty() || "/".equals(normalized)) {
            return "";
        }
        return normalized.startsWith("/") ? normalized : "/" + normalized;
    }

    private static String now() {
        return Instant.now().toString();
    }

    private static ErrorResponse errorResponse(String error, String message, String path) {
        return new ErrorResponse(error, message, now(), path);
    }

    private static String readBody(InputStream requestBody) throws IOException {
        return new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);
    }

    private static void sendResponse(HttpExchange exchange, int statusCode, Object payload) throws IOException {
        byte[] responseBytes = JsonUtils.toJson(payload).getBytes(StandardCharsets.UTF_8);
        Headers responseHeaders = exchange.getResponseHeaders();
        responseHeaders.add("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(responseBytes);
        }
    }

    private static boolean isAuthorizationRequired() {
        return ConfigReader.getBooleanProperty("api.auth.enabled", true);
    }

    private static boolean isAuthorized(HttpExchange exchange) {
        if (!isAuthorizationRequired()) {
            return true;
        }
        String headerName = ConfigReader.getProperty("api.auth.tokenHeader", "Authorization");
        String prefix = ConfigReader.getProperty("api.auth.tokenPrefix", "Bearer");
        String authHeader = exchange.getRequestHeaders().getFirst(headerName);
        return (prefix + " " + MOCK_ACCESS_TOKEN).equals(authHeader);
    }

    private static final class AuthHandler implements HttpHandler {

        private final String endpointPath;

        private AuthHandler(String endpointPath) {
            this.endpointPath = endpointPath;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                    sendResponse(exchange, 405, errorResponse("Method Not Allowed", "Only POST is supported.", endpointPath));
                    return;
                }

                AuthRequest request = JsonUtils.fromJson(readBody(exchange.getRequestBody()), AuthRequest.class);
                String expectedClientId = ConfigReader.getRequiredProperty("api.auth.clientId");
                String expectedClientSecret = ConfigReader.getRequiredProperty("api.auth.clientSecret");

                if (!expectedClientId.equals(request.getClientId()) || !expectedClientSecret.equals(request.getClientSecret())) {
                    sendResponse(exchange, 401, errorResponse("Unauthorized", "Invalid API client credentials.", endpointPath));
                    return;
                }

                AuthResponse response = new AuthResponse();
                response.setAccessToken(MOCK_ACCESS_TOKEN);
                response.setTokenType(ConfigReader.getProperty("api.auth.tokenPrefix", "Bearer"));
                response.setExpiresIn(3600);
                sendResponse(exchange, 200, response);
            } catch (ApiException exception) {
                sendResponse(exchange, 400, errorResponse("Bad Request", exception.getMessage(), endpointPath));
            } catch (Exception exception) {
                sendResponse(exchange, 500, errorResponse("Server Error", exception.getMessage(), endpointPath));
            } finally {
                exchange.close();
            }
        }
    }

    private static final class UserHandler implements HttpHandler {

        private final String endpointPath;

        private UserHandler(String endpointPath) {
            this.endpointPath = endpointPath;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String requestPath = exchange.getRequestURI().getPath();
            try {
                if (!isAuthorized(exchange)) {
                    sendResponse(exchange, 401, errorResponse("Unauthorized", "Missing or invalid bearer token.", requestPath));
                    return;
                }

                if ("POST".equalsIgnoreCase(exchange.getRequestMethod()) && endpointPath.equals(requestPath)) {
                    handleCreateUser(exchange, requestPath);
                    return;
                }

                if ("GET".equalsIgnoreCase(exchange.getRequestMethod()) && requestPath.startsWith(endpointPath + "/")) {
                    String idValue = requestPath.substring(endpointPath.length() + 1);
                    handleGetUser(exchange, requestPath, idValue);
                    return;
                }

                sendResponse(exchange, 404, errorResponse("Not Found", "Requested resource does not exist.", requestPath));
            } catch (ApiException exception) {
                sendResponse(exchange, 400, errorResponse("Bad Request", exception.getMessage(), requestPath));
            } catch (Exception exception) {
                sendResponse(exchange, 500, errorResponse("Server Error", exception.getMessage(), requestPath));
            } finally {
                exchange.close();
            }
        }

        private void handleCreateUser(HttpExchange exchange, String requestPath) throws IOException {
            UserRequest request = JsonUtils.fromJson(readBody(exchange.getRequestBody()), UserRequest.class);
            validateUserRequest(request);

            UserResponse response = new UserResponse();
            response.setId(USER_ID_SEQUENCE.incrementAndGet());
            response.setFirstName(request.getFirstName());
            response.setLastName(request.getLastName());
            response.setEmail(request.getEmail());
            response.setUsername(request.getUsername());
            response.setPhone(request.getPhone());
            response.setCompany(request.getCompany());
            response.setCreatedAt(now());

            USERS.put(response.getId(), response);
            sendResponse(exchange, 201, response);
        }

        private void handleGetUser(HttpExchange exchange, String requestPath, String idValue) throws IOException {
            int userId;
            try {
                userId = Integer.parseInt(idValue);
            } catch (NumberFormatException exception) {
                sendResponse(exchange, 400, errorResponse("Bad Request", "User id must be numeric.", requestPath));
                return;
            }

            UserResponse storedUser = USERS.get(userId);
            if (storedUser == null) {
                sendResponse(exchange, 404, errorResponse("Not Found", "User not found for id: " + userId, requestPath));
                return;
            }

            sendResponse(exchange, 200, storedUser);
        }

        private void validateUserRequest(UserRequest request) {
            if (request == null) {
                throw new ApiException("User payload cannot be null.");
            }

            validateField(request.getFirstName(), "firstName");
            validateField(request.getLastName(), "lastName");
            validateField(request.getEmail(), "email");
            validateField(request.getUsername(), "username");
            validateField(request.getPhone(), "phone");
            validateField(request.getCompany(), "company");
        }

        private void validateField(String value, String fieldName) {
            if (value == null || value.isBlank()) {
                throw new ApiException("Missing mandatory field: " + fieldName);
            }
        }
    }
}

