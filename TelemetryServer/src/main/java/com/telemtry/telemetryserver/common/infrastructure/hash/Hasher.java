package com.telemtry.telemetryserver.common.infrastructure.hash;

import org.springframework.context.annotation.Configuration;

@Configuration
public interface Hasher {

    String getHash(String secureKey);

}
