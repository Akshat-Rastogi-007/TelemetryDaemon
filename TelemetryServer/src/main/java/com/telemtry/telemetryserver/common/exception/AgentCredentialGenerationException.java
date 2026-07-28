package com.telemtry.telemetryserver.common.exception;

public class AgentCredentialGenerationException extends RuntimeException {

    public AgentCredentialGenerationException(String message, Exception e) {
        super(message,e);
    }
}
