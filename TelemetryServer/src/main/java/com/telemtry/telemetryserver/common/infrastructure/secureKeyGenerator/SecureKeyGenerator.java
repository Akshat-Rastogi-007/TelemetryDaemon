package com.telemtry.telemetryserver.common.infrastructure.secureKeyGenerator;

import org.springframework.context.annotation.Configuration;

@Configuration
public interface SecureKeyGenerator {

    String generateSecureKey();


}
