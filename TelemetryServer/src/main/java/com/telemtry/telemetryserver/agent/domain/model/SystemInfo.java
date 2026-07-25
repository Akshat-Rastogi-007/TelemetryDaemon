package com.telemtry.telemetryserver.agent.domain.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemInfo {

    private String javaVersion;
    private String operatingSystem;
    private String architecture;

}
