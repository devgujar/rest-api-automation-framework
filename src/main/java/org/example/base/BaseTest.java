package org.example.base;

import io.restassured.RestAssured;
import org.example.config.ConfigManager;
import org.testng.annotations.BeforeMethod;

public class BaseTest {

    @BeforeMethod
    public void setUpBaseURI() {

        RestAssured.baseURI = ConfigManager.getProperty("base.uri");

        if (Boolean.parseBoolean(ConfigManager.get("ssl.relaxed", "true"))) {
            RestAssured.useRelaxedHTTPSValidation();
        }
    }

}

