package api.core;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import api.auth.AuthProvider;
import api.config.ConfigManager;

/**
 * Centralised builders for Request/Response specs.
 * Headers, base URI, timeouts, auth, logging filters live here once.
 */
public final class SpecFactory {

    private SpecFactory() {}

    public static RequestSpecification request() {
        int connTimeout = Integer.parseInt(ConfigManager.get("http.connect.timeout.ms", "10000"));
        int readTimeout = Integer.parseInt(ConfigManager.get("http.read.timeout.ms", "30000"));

        RestAssuredConfig cfg = RestAssuredConfig.config().httpClient(
                HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", connTimeout)
                        .setParam("http.socket.timeout", readTimeout));

        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(ConfigManager.get("base.uri"))
                .setBasePath(ConfigManager.get("base.path", ""))
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .setConfig(cfg);

        // Conditionally add logging filters based on property
        boolean enableLogging = Boolean.parseBoolean(ConfigManager.get("enable.logging", "false"));
        if (enableLogging) {
            builder.addFilter(new RequestLoggingFilter());
            builder.addFilter(new ResponseLoggingFilter());
        }

        RequestSpecification spec = builder.build();
        return AuthProvider.applyAuthentication(spec);
    }

    public static ResponseSpecification responseOk() {
        return new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .build();
    }
}

