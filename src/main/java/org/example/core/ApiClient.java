package org.example.core;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import java.util.Map;

public class ApiClient {

    // Main method supporting headers and query params
    public static Response sendRequest(String method, String path, String payload,
                                       Map<String, String> headers,
                                       Map<String, String> queryParams) {

        RequestSpecification request = given();

        request.spec(SpecFactory.request());

        if (headers != null) {
            request.headers(headers);
        }

        if (queryParams != null) {
            request.queryParams(queryParams);
        }

        if (payload != null) {
            request.header("Content-Type", "application/json");
            request.body(payload);
        }

        if (method.equalsIgnoreCase("GET")) {
            request.header("Accept", "application/json");
        }

        return request
                .when()
                .request(method, path)
                .then()
                .extract().response();
    }


    public static Response sendRequest(String method, String path) {
        return sendRequest(method, path, null, null, null);
    }
    // Overloaded convenience methods
    public static Response sendRequest(String method, String path, String payload) {
        return sendRequest(method, path, payload, null, null);
    }
    public static Response sendRequest(String method, String path, Map<String, String> queryParams) {
        return sendRequest(method, path, null, null, queryParams);
    }
}
