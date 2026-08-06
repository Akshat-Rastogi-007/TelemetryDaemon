package com.telemtry.telemetryserver.agent.api.controller;

import com.telemtry.telemetryserver.agent.api.response.AgentResponseDto;
import com.telemtry.telemetryserver.agent.application.AgentService;
import com.telemtry.telemetryserver.common.domain.ApiResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/dashboard/agent/")
public class AgentController {

    private final AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }



    @GetMapping("/all-agents/")
    public ResponseEntity<ApiResponseDto<?>> getAllAgents(){

        List<AgentResponseDto> allAgents = agentService.getAllAgents();

        return new ResponseEntity<>(
                new ApiResponseDto<>(
                        allAgents,
                        HttpStatus.OK,
                        "Agents Retrieved"
                )
                , HttpStatus.OK
        );


    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<?>> getAgentById(@PathVariable String id) {


        AgentResponseDto agentResponseDto = agentService.findByPublicId(id);

        return new ResponseEntity<>(
                new ApiResponseDto<>(
                        agentResponseDto,
                        HttpStatus.OK,
                        "Agent Retrieved"
                )
                , HttpStatus.OK
        );

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<?>> deleteAgent(@PathVariable String id){

        agentService.deleteAgent(id);

        return new ResponseEntity<>(
                new ApiResponseDto<>(
                        null,
                        HttpStatus.OK,
                        "Agent deleted successfully."
                ),
                HttpStatus.OK
        );
        
    }

    @DeleteMapping("/all-agents/")
    public ResponseEntity<ApiResponseDto<?>> deleteAllAgents() {

        agentService.deleteAllAgents();

        return new ResponseEntity<>(
                new ApiResponseDto<>(
                        null,
                        HttpStatus.OK,
                        "All agents deleted successfully."
                ),
                HttpStatus.OK
        );

    }

}
