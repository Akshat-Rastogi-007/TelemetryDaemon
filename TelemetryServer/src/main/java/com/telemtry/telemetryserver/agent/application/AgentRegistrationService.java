package com.telemtry.telemetryserver.agent.application;

import com.telemtry.telemetryserver.agent.api.request.AgentRequestDto;
import com.telemtry.telemetryserver.agent.api.response.AgentResponseDto;
import com.telemtry.telemetryserver.agent.domain.model.Agent;
import com.telemtry.telemetryserver.agent.domain.model.AgentStatus;
import com.telemtry.telemetryserver.agent.domain.model.SystemInfo;
import com.telemtry.telemetryserver.agent.domain.repository.AgentRepository;
import com.telemtry.telemetryserver.common.exception.ResourceAlreadyExistsException;
import com.telemtry.telemetryserver.common.infrastructure.hash.Hasher;
import com.telemtry.telemetryserver.common.infrastructure.secureKeyGenerator.SecureKeyGenerator;
import com.telemtry.telemetryserver.user.api.CurrentUserProvider;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AgentRegistrationService {


    private final AgentRepository agentRepository;
    private final SecureKeyGenerator credentialGenerator;
    private final Hasher credentialHasher;
    private final CurrentUserProvider currentUserProvider;

    public AgentRegistrationService(
            @Qualifier("jpaAgentRepository")
            AgentRepository agentRepository,
            SecureKeyGenerator credentialGenerator,
            Hasher credentialHasher,
            @Qualifier("patSecurityCurrentUserProvider")
            CurrentUserProvider currentUserProvider) {
        this.agentRepository = agentRepository;
        this.credentialGenerator = credentialGenerator;
        this.credentialHasher = credentialHasher;
        this.currentUserProvider = currentUserProvider;
    }


    @Transactional
    public AgentResponseDto registerAgent(AgentRequestDto dto){

        long userId = currentUserProvider.currentUser().getId();

        Optional<Agent> optionalAgent = agentRepository.findByInstallationId(dto.getInstallationId());
        if (optionalAgent.isPresent()){
            throw new ResourceAlreadyExistsException("Agent is already registered");
        }
        Agent agent = mapToAgent(dto, userId);
        agent.setRegisteredAt(
                LocalDateTime.now()
        );
        agent.setStatus(AgentStatus.OFFLINE);

        String secureKey = "agt_" + credentialGenerator.generateSecureKey();

        String hashedKey =  credentialHasher.getHash(secureKey);

        agent.setSecureHashToken(hashedKey);
        Agent savedAgent = agentRepository.save(agent);

        return mapToAgentResponseDto(savedAgent, secureKey);

    }




    private Agent mapToAgent(AgentRequestDto dto, long userId){

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


    @NotNull
    private static AgentResponseDto mapToAgentResponseDto(Agent savedAgent, String secureKey) {
        AgentResponseDto agentResponseDto = new AgentResponseDto();
        agentResponseDto.setAgentId(savedAgent.getPublicId());
        agentResponseDto.setInstallationId(savedAgent.getInstallationId());
        agentResponseDto.setSecureKey(secureKey);

        return agentResponseDto;
    }

}
