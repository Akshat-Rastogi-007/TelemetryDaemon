package com.telemtry.telemetryserver.common.infrastructure.secureKeyGenerator;

import org.springframework.context.annotation.Configuration;

import java.security.SecureRandom;
import java.util.Base64;


@Configuration("secureRandomKeyGenerator")
public class SecureRandomKeyGenerator implements SecureKeyGenerator {


    @Override
    public String generateSecureKey() {


        SecureRandom secureRandom = new SecureRandom();

        byte[] bytes = new byte[32];

        secureRandom.nextBytes(bytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);

    }
}
