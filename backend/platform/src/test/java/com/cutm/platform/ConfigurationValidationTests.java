package com.cutm.platform;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ConfigurationValidationTests {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${server.port}")
    private int serverPort;

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Test
    void backendCorePropertiesAreConfigured() {
        assertEquals("platform", applicationName, "Unexpected spring.application.name value");
        assertTrue(serverPort > 0, "server.port must be a positive integer");
        assertNotNull(mongoUri, "MongoDB URI must be configured");
        assertFalse(mongoUri.isBlank(), "MongoDB URI must not be blank");
        assertTrue(mongoUri.startsWith("mongodb://") || mongoUri.startsWith("mongodb+srv://"),
                "MongoDB URI must use mongodb:// or mongodb+srv:// scheme");
    }
}
