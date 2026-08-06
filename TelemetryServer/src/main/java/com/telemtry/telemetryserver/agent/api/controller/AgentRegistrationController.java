package com.telemtry.telemetryserver.agent.api.controller;


import com.telemtry.telemetryserver.agent.api.request.AgentRequestDto;
import com.telemtry.telemetryserver.agent.api.response.AgentResponseDto;
import com.telemtry.telemetryserver.agent.application.AgentRegistrationService;
import com.telemtry.telemetryserver.common.domain.ApiResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/agent/")
public class AgentRegistrationController {


    private final AgentRegistrationService agentRegistrationService;

    public AgentRegistrationController(AgentRegistrationService agentRegistrationService) {
        this.agentRegistrationService = agentRegistrationService;
    }


    @PostMapping("/register-agent/")
    public ResponseEntity<ApiResponseDto<?>> registerAgent(@RequestBody AgentRequestDto dto){

        AgentResponseDto agentResponseDto = agentRegistrationService.registerAgent(dto);


        return new ResponseEntity<>(

                new ApiResponseDto<>(agentResponseDto, HttpStatus.CREATED,"Agent Registered")

                , HttpStatus.CREATED
        );

    }

}
