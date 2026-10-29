package com.telemtry.telemetryserver.agent.api.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollectorRequestDto {

    private String id;
    private boolean isEnabled;
}
