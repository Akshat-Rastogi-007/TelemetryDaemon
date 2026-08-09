package com.telemtry.telemetryserver.agent.api.controller;

import com.telemtry.telemetryserver.agent.api.response.AgentResponseDto;
import com.telemtry.telemetryserver.agent.application.AgentService;
import com.telemtry.telemetryserver.common.domain.ApiResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/dashboard/agent/")
public class AgentController {

    private final AgentService agentService;

    private static final Logger logger = LoggerFactory.getLogger(AgentController.class);

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }



    @GetMapping("/all-agents/")
    public ResponseEntity<ApiResponseDto<?>> getAllAgents(){
        logger.info("Fetching all agents for authenticated user.");

        List<AgentResponseDto> allAgents = agentService.getAllAgents();

        logger.info("Retrieved {} agent(s).", allAgents.size());

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

        logger.info("Fetching agent with publicId={}", id);

        AgentResponseDto agentResponseDto = agentService.findByPublicId(id);

        logger.info("Successfully retrieved agent with publicId={}", id);

        return new ResponseEntity<>(
                new ApiResponseDto<>(
                        agentResponseDto,
                        HttpStatus.OK,
                        "Agent Retrieved"
                ),
                HttpStatus.OK
        );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<?>> deleteAgent(@PathVariable String id){

        logger.info("Deleting agent with publicId={}", id);

        agentService.deleteAgent(id);

        logger.info("Agent with publicId={} deleted successfully.", id);

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

        logger.warn("Deleting all agents for authenticated user.");

        agentService.deleteAllAgents();

        logger.info("All agents deleted successfully.");

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
