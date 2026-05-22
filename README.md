# API Automation Framework (Java + TestNG + RestAssured)

## Overview
This project is a simple, extensible API testing automation framework built using Java, TestNG, and RestAssured. It is designed for easy configuration, maintainability, and scalability for REST API testing.

## Project Structure
```
ApiAutomation/
├── pom.xml
├── README.md
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── api/
│   │   │       ├── auth/
│   │   │       │   └── AuthProvider.java
│   │   │       ├── base/
│   │   │       │   └── BaseTest.java
│   │   │       ├── config/
│   │   │       │   └── ConfigManager.java
│   │   │       └── core/
│   │   │           ├── ApiClient.java
│   │   │           └── SpecFactory.java
│   │   └── resources/
│   │       └── baseConfig.properties
│   └── test/
│       └── java/
│           └── api/tests/
│               └── SampleApiTest.java
└── target/
```

## Key Components

### 1. ApiClient (core/ApiClient.java)
- Central class for sending HTTP requests using RestAssured.
- Supports all HTTP methods, headers, query parameters, and payloads.
- Logging filters can be enabled/disabled via configuration.

### 2. SpecFactory (core/SpecFactory.java)
- Provides reusable request/response specifications for RestAssured.

### 3. BaseTest (base/BaseTest.java)
- Abstract base class for all test classes.
- Sets up the base URI before each test using `ConfigManager`.
- Ensures consistent environment setup for all tests.

### 4. ConfigManager (config/ConfigManager.java)
- Loads configuration properties from `baseConfig.properties`.
- Provides easy access to configuration values (e.g., base URI, logging flags).

### 5. AuthProvider (auth/AuthProvider.java)
- Provides authentication utilities for API requests.

### 6. SampleApiTest
- Example test class demonstrating how to use the framework.
- Extends `BaseTest` and uses `ApiClient` for API calls.

## Configuration
- All configuration is managed in `src/main/resources/baseConfig.properties`.
- Example properties:
  ```properties
  baseURI=https://jsonplaceholder.typicode.com
  enableLogging=true
  ```

## How to Add Logging Filters Conditionally
- The `ApiClient` checks the `enableLogging` property from `ConfigManager`.
- If enabled, it adds `RequestLoggingFilter` and `ResponseLoggingFilter` to the RestAssured request.

## Example Test Usage
```java
Response response = ApiClient.sendRequest("GET", "/posts/1");
Assert.assertEquals(response.getStatusCode(), 200);
```

## How to Check for a Specific Title for a User
- Parse the JSON response and filter by `userId` and `title`.
- Example (using JsonPath):
  ```java
  Response response = ApiClient.sendRequest("GET", "/posts");
  List<Map<String, Object>> posts = response.jsonPath().getList("");
  boolean found = posts.stream().anyMatch(post ->
      post.get("userId").equals(5) && post.get("title").equals("your title here")
  );
  Assert.assertTrue(found);
  ```

## SLF4J Warning
- If you see `SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder"`, add an SLF4J binding (e.g., `slf4j-simple` or `logback-classic`) to your dependencies.

## Running the Tests
- Use your IDE or run via Maven:
  ```sh
  mvn clean test
  ```

## Extending the Framework
- Add new test classes under `src/test/java/api/tests/` and extend `BaseTest`.
- Use `ApiClient` for all HTTP requests.
- Add new configuration keys to `baseConfig.properties` as needed.

---

This framework provides a clean, maintainable starting point for REST API automation in Java. You can easily extend it for more advanced scenarios, authentication, reporting, and more.
