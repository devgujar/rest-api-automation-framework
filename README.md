# API Automation Framework (Java + TestNG + RestAssured)

## Overview
This project is a modular, extensible API testing automation framework built using Java, TestNG, and RestAssured. It is designed for easy configuration, maintainability, and scalability for REST API testing.

## Project Structure
```
ApiAutomation/
├── pom.xml
├── README.md
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── api/
│   │   │       ├── base/
│   │   │       │   ├── BaseApiClient.java
│   │   │       │   └── BaseTest.java
│   │   │       ├── clients/
│   │   │       │   └── CustomerApiClient.java
│   │   │       ├── config/
│   │   │       │   └── ConfigManager.java
│   │   │       └── core/
│   │   │           ├── AuthProvider.java
│   │   │           └── SpecFactory.java
│   │   └── resources/
│   │       └── baseConfig.properties
│   └── test/
│       └── java/
│           └── api/tests/
│               └── CustomerApiTest.java
└── target/
```

## Key Components

### 1. BaseApiClient (`base/BaseApiClient.java`)
- Central class for sending HTTP requests using RestAssured.
- Supports all HTTP methods, headers, query parameters, and payloads.
- Uses request/response specifications from SpecFactory.

### 2. CustomerApiClient (`clients/CustomerApiClient.java`)
- Domain-specific client for customer-related API operations (create, update, delete, get, etc.).
- Provides utility methods for payload generation and record existence checks.

### 3. SpecFactory (`core/SpecFactory.java`)
- Provides reusable request/response specifications for RestAssured.
- Conditionally adds logging filters based on configuration.

### 4. BaseTest (`base/BaseTest.java`)
- Abstract base class for all test classes.
- Sets up the base URI and SSL settings before each test using ConfigManager.

### 5. ConfigManager (`config/ConfigManager.java`)
- Loads configuration properties from `baseConfig.properties`.
- Provides easy access to configuration values (e.g., base URI, logging flags).

### 6. AuthProvider (`core/AuthProvider.java`)
- Provides authentication utilities for API requests, supporting multiple auth types.

### 7. CustomerApiTest (test class)
- Example test class demonstrating how to use the framework for customer APIs.
- Extends `BaseTest` and uses `CustomerApiClient` for API calls.

## Configuration
- All configuration is managed in `src/main/resources/baseConfig.properties`.
- Example properties:
  ```properties
  base.uri = https://restapi.devgujar.workers.dev
  enable.logging = false
  ssl.relaxed = true
  auth.type = NONE
  ```

## Conditional Logging
- Logging filters (`RequestLoggingFilter`, `ResponseLoggingFilter`) are added only if `enable.logging=true` in the properties file.
- This is handled in `SpecFactory.java`.
- Example property to disable logging:
  ```properties
  enable.logging = false
  ```

## Example Test Usage
```java
CustomerApiClient client = new CustomerApiClient();
String payload = client.getDefaultCustomerPayload();
Response response = client.createCustomer(payload);
Assert.assertEquals(response.getStatusCode(), 201);
```

## How to Check for a Specific Customer Record
- Parse the JSON response and filter by `id`, `name`, and address fields.
- Example (using JsonPath):
  ```java
  Response response = BaseApiClient.sendRequest("GET", "/customer");
  List<Map<String, Object>> customers = response.jsonPath().getList("");

  boolean found = customers.stream().anyMatch(customer ->
          customer.get("id").equals("1000") 
          &&  customer.get("name").equals("testUser1000")
          && customer.get("address").toString().contains("country=India")
  );

  Assert.assertTrue(found);
  ```

## SLF4J Warning
- If you see `SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder"`, add an SLF4J binding (e.g., `slf4j-simple` or `logback-classic`) to your dependencies. This is already included in the pom.xml.

## Running the Tests
- Use your IDE or run via Maven:
  ```sh
  mvn clean test
  ```

## Extending the Framework
- Add new domain clients under `src/main/java/api/clients/` for other API resources.
- Add new test classes under `src/test/java/api/tests/` and extend `BaseTest`.
- Use `BaseApiClient` or your domain client for all HTTP requests.
- Add new configuration keys to `baseConfig.properties` as needed.

---

This framework provides a clean, maintainable starting point for REST API automation in Java. You can easily extend it for more advanced scenarios, authentication, reporting, and more.
