package com.telemtry.telemetryserver.agent.api;

import com.telemtry.telemetryserver.agent.api.request.AgentRequestDto;
import com.telemtry.telemetryserver.agent.api.response.AgentResponseDto;
import com.telemtry.telemetryserver.agent.application.AgentService;
import com.telemtry.telemetryserver.common.domain.ApiResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/agent/")
public class AgentController {

    private final AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }


    @PostMapping("/register-agent/")
    public ResponseEntity<ApiResponseDto<?>> registerAgent(@RequestBody AgentRequestDto dto){

        AgentResponseDto agentResponseDto = agentService.registerAgent(dto);


        return new ResponseEntity<>(

                new ApiResponseDto<>(agentResponseDto,HttpStatus.CREATED,"Agent Registered")

                , HttpStatus.CREATED
        );

    }
}
