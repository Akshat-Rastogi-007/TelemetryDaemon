package com.telemtry.telemetryserver.agent.application;

import com.telemtry.telemetryserver.agent.api.response.AgentResponseDto;
import com.telemtry.telemetryserver.agent.api.request.AgentRequestDto;
import com.telemtry.telemetryserver.agent.domain.model.Agent;
import com.telemtry.telemetryserver.agent.domain.model.AgentStatus;
import com.telemtry.telemetryserver.agent.domain.model.SystemInfo;
import com.telemtry.telemetryserver.agent.domain.repository.AgentRepository;
import com.telemtry.telemetryserver.agent.infrastructure.secureKeyGenerator.AgentCredentialGenerator;
import com.telemtry.telemetryserver.common.infrastructure.hash.AgentCredentialHasher;
import com.telemtry.telemetryserver.common.exception.ResourceAlreadyExistsException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AgentService {

    private final AgentRepository agentRepository;
    private final AgentCredentialGenerator credentialGenerator;
    private final AgentCredentialHasher credentialHasher;


    public AgentService(
            @Qualifier("jpaAgentRepository")
            AgentRepository agentRepository,
            @Qualifier("secureRandomCredentialGenerator")
            AgentCredentialGenerator credentialGenerator,
            @Qualifier("sha256CredentialHasher")
            AgentCredentialHasher credentialHasher) {

        this.agentRepository = agentRepository;
        this.credentialGenerator = credentialGenerator;
        this.credentialHasher = credentialHasher;
    }

    @Transactional
    public AgentResponseDto registerAgent(AgentRequestDto dto){

        Optional<Agent> optionalAgent = agentRepository.findByInstallationId(dto.getInstallationId());
        if (optionalAgent.isPresent()){
            throw new ResourceAlreadyExistsException("Agent is already registered");
        }
        Agent agent = mapToAgent(dto, "admin-123");
        agent.setRegisteredAt(
                LocalDateTime.now()
        );
        agent.setStatus(AgentStatus.OFFLINE);

        String secureKey = credentialGenerator.generateSecureKey();
        String hashedKey = credentialHasher.getHash(secureKey);

        agent.setSecureHash(hashedKey);
        Agent savedAgent = agentRepository.save(agent);

        AgentResponseDto agentResponseDto = new AgentResponseDto();
        agentResponseDto.setAgentId(savedAgent.getId());
        agentResponseDto.setSecureKey(secureKey);

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
