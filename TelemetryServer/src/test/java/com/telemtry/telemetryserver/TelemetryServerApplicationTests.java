package com.telemtry.telemetryserver;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.modulith.core.ApplicationModules;

@SpringBootTest
class TelemetryServerApplicationTests {

    @Test
    void contextLoads() {
    }


    @Test
    void verifyModularStructure() {
        // Replace 'MyApplication.class' with your main @SpringBootApplication class
        ApplicationModules modules = ApplicationModules.of(TelemetryServerApplication.class);

        // Analyzes dependencies and throws an exception if rules are broken
        modules.verify();
    }
}
