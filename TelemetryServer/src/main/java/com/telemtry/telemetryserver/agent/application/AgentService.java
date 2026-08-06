package com.telemtry.telemetryserver.agent.application;

import com.telemtry.telemetryserver.agent.api.response.AgentResponseDto;
import com.telemtry.telemetryserver.agent.domain.model.Agent;
import com.telemtry.telemetryserver.agent.domain.repository.AgentRepository;
import com.telemtry.telemetryserver.common.exception.ResourceNotFoundException;
import com.telemtry.telemetryserver.user.api.CurrentUserProvider;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
public class AgentService {

    private final AgentRepository agentRepository;
    private final CurrentUserProvider currentUserProvider;


    public AgentService(
            @Qualifier("jpaAgentRepository")
            AgentRepository agentRepository,
            @Qualifier("userSecurityCurrentUserProvider")
            CurrentUserProvider currentUserProvider) {

        this.agentRepository = agentRepository;
        this.currentUserProvider = currentUserProvider;
    }


    @NotNull
    private static AgentResponseDto mapToAgentResponseDto(Agent savedAgent) {
        AgentResponseDto agentResponseDto = new AgentResponseDto();
        agentResponseDto.setAgentId(savedAgent.getPublicId());
        agentResponseDto.setInstallationId(savedAgent.getInstallationId());
        agentResponseDto.setSecureKey("");

        return agentResponseDto;
    }

    public List<AgentResponseDto> getAllAgents() {

        long userId = currentUserProvider.currentUser().getId();


        return agentRepository.
                findAgentsByUserId(userId)
                .stream()
                .map(AgentService::mapToAgentResponseDto
                ).toList();

    }

    public void deleteAllAgents() {
        agentRepository.deleteAll();
    }

    public Optional<Agent> findBySecureToken(String secureToken){

        return agentRepository.findBySecureToken(secureToken);

    }

    public AgentResponseDto findByPublicId(String publicId) {

        Agent agent = agentRepository
                .findByPublicId(publicId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Agent not found")
                );


        return mapToAgentResponseDto(agent);

    }

    public void deleteAgent(String id) {

        agentRepository.delete(id);
    }
}
