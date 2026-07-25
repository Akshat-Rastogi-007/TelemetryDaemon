package com.telemtry.telemetryserver.agent.domain.service;

import com.telemtry.telemetryserver.agent.api.AgentResponseDto;
import com.telemtry.telemetryserver.agent.api.request.AgentRequestDto;
import com.telemtry.telemetryserver.agent.domain.model.Agent;
import com.telemtry.telemetryserver.agent.domain.model.AgentStatus;
import com.telemtry.telemetryserver.agent.domain.model.SystemInfo;
import com.telemtry.telemetryserver.agent.domain.repository.AgentRepository;
import com.telemtry.telemetryserver.common.exception.ResourceAlreadyExistsException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AgentService {

    private final AgentRepository agentRepository;


    public AgentService(
            @Qualifier("jpaAgentRepository")
            AgentRepository agentRepository) {

        this.agentRepository = agentRepository;
    }


    public AgentResponseDto registerAgent(AgentRequestDto dto){

        Optional<Agent> optionalAgent = agentRepository.findByInstallationId(dto.getSecureHash());

        if (optionalAgent.isPresent()){

            throw new ResourceAlreadyExistsException("Agent is already registered");

        }

        Agent agent = mapToAgent(dto, "admin-123");

        agent.setRegisteredAt(
                LocalDateTime.now()
        );

        agent.setStatus(AgentStatus.OFFLINE);

        Agent savedAgent = agentRepository.save(agent);


//        String hashKey = secureKeyGenerator.generate();

        AgentResponseDto agentResponseDto = new AgentResponseDto();

        agentResponseDto.setAgentId(savedAgent.getId());

        return agentResponseDto;

    }


    private Agent mapToAgent(AgentRequestDto dto, String userId){

        Agent agent = new Agent();

        SystemInfo systemInfo = new SystemInfo(dto.getJavaVersion(), dto.getOperatingSystem(), dto.getArchitecture());
        agent.setDeviceName(dto.getDeviceName());
        agent.setAuthType(dto.getAuthType());
        agent.setVersion(dto.getVersion());
        agent.setSystemInfo(systemInfo);
        agent.setInstallationId(dto.getInstallationId());
        agent.setUserId(userId);

        return agent;
    }


}
