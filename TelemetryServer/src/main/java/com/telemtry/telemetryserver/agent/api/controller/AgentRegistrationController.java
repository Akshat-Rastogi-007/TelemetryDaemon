package com.telemtry.telemetryserver.agent.api.controller;


import com.telemtry.telemetryserver.agent.api.request.AgentRequestDto;
import com.telemtry.telemetryserver.agent.api.response.AgentResponseDto;
import com.telemtry.telemetryserver.agent.application.AgentRegistrationService;
import com.telemtry.telemetryserver.common.domain.ApiResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/agent/")
public class AgentRegistrationController {


    private final AgentRegistrationService agentRegistrationService;

    private static final Logger logger =
            LoggerFactory.getLogger(AgentRegistrationController.class);

    public AgentRegistrationController(AgentRegistrationService agentRegistrationService) {
        this.agentRegistrationService = agentRegistrationService;
    }


    @PostMapping("/register-agent/")
    public ResponseEntity<ApiResponseDto<?>> registerAgent(@RequestBody AgentRequestDto dto){


        logger.info(
                "Received agent registration request. InstallationId={}",
                dto.getInstallationId()
        );


        AgentResponseDto agentResponseDto = agentRegistrationService.registerAgent(dto);


        return new ResponseEntity<>(

                new ApiResponseDto<>(agentResponseDto, HttpStatus.CREATED,"Agent Registered")

                , HttpStatus.CREATED
        );

    }

}
