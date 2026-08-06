package com.telemtry.telemetryserver.telemetry.api.controller;


import com.telemtry.telemetryserver.common.domain.ApiResponseDto;
import com.telemtry.telemetryserver.telemetry.api.request.TelemetryBatchRequest;
import com.telemtry.telemetryserver.telemetry.application.AgentTelemetryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/app/agent/telemetry/")
public class AgentTelemetryController {


    private final AgentTelemetryService agentTelemetryService;

    public AgentTelemetryController(AgentTelemetryService agentTelemetryService) {
        this.agentTelemetryService = agentTelemetryService;
    }

    @PostMapping("/submit/")
    public ResponseEntity<?> submitTelemetryData(@RequestBody TelemetryBatchRequest batchRequest){

        agentTelemetryService.submitTelemetry(batchRequest);

        return new ResponseEntity<ApiResponseDto<?>>(
                new ApiResponseDto<>(Collections.EMPTY_MAP, HttpStatus.OK,"Message Received"),
                HttpStatus.OK);
    }

}
