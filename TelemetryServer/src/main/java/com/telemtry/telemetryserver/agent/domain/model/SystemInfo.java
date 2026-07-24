package com.telemtry.telemetryserver.agent.domain.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class SystemInfo {

    private String javaVersion;
    private String operatingSystem;
    private String architecture;

}
