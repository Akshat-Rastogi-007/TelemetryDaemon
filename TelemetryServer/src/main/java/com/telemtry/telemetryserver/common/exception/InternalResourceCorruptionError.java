package com.telemtry.telemetryserver.common.exception;

public class InternalResourceCorruptionError extends RuntimeException {

    public InternalResourceCorruptionError(String message) {
        super(message);
    }

    public InternalResourceCorruptionError(String message, Exception e) {
        super(message,e);
    }

}
