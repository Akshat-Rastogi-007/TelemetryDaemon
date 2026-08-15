package com.telemtry.telemetryserver.agent.api.controller;


import com.telemtry.telemetryserver.agent.application.AgentConfigurationService;
import com.telemtry.telemetryserver.common.domain.ApiResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RequestMapping("/app/agent/heartbeat/")
@RestController
public class AgentHeartbeatController {


    private final AgentConfigurationService agentConfigurationService;


    public AgentHeartbeatController(AgentConfigurationService agentConfigurationService) {
        this.agentConfigurationService = agentConfigurationService;
    }


    @PutMapping("/")
    public ResponseEntity<ApiResponseDto<?>> heartbeatCheck(){

        agentConfigurationService.agentHeartbeatCheck();

        return new ResponseEntity<>(
                new ApiResponseDto<>(
                        Collections.EMPTY_MAP,
                        HttpStatus.OK,
                        "Heartbeat check successful"
                ),
                HttpStatus.OK

        );
    }
}
