package com.telemtry.telemetryserver.agent.infrastructure.security.secureKeyGenerator;

import com.telemtry.telemetryserver.agent.infrastructure.security.AgentCredentialGenerator;
import org.springframework.context.annotation.Configuration;

import java.security.SecureRandom;
import java.util.Base64;


@Configuration("secureRandomCredentialGenerator")
public class SecureRandomCredentialGenerator implements AgentCredentialGenerator {


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
