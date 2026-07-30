package com.telemtry.telemetryserver.common.infrastructure.hash.secureKeyHashService;

import com.telemtry.telemetryserver.common.exception.AgentCredentialGenerationException;
import com.telemtry.telemetryserver.common.infrastructure.hash.Hasher;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;

@Component("sha256Hasher")
public class Sha256Hasher implements Hasher {


    @Override
    public String getHash(String secureKey) {

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(secureKey.getBytes(StandardCharsets.UTF_8));

            return HexFormat.of().formatHex(hash);

        }
        catch (Exception e) {
            throw new AgentCredentialGenerationException(
                    "Failed to generate agent credential", e
            );
        }
    }


}
