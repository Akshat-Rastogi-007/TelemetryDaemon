package com.telemtry.telemetryserver.agent.application;

import com.telemtry.telemetryserver.agent.api.response.AgentResponseDto;
import com.telemtry.telemetryserver.agent.domain.model.Agent;
import com.telemtry.telemetryserver.agent.domain.repository.AgentRepository;
import com.telemtry.telemetryserver.common.exception.ResourceNotFoundException;
import com.telemtry.telemetryserver.user.api.CurrentUserProvider;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class AgentService {

    private static final Logger logger = LoggerFactory.getLogger(AgentService.class);
    private final AgentRepository agentRepository;
    private final CurrentUserProvider currentUserProvider;


    public AgentService(@Qualifier("jpaAgentRepository") AgentRepository agentRepository, @Qualifier("userSecurityCurrentUserProvider") CurrentUserProvider currentUserProvider) {

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

        logger.info("Fetching all agents for authenticated user. userId={}", userId);

        List<AgentResponseDto> agents = agentRepository.findAgentsByUserId(userId).stream().map(AgentService::mapToAgentResponseDto).toList();

        logger.info("Retrieved {} agent(s) for authenticated user. userId={}", agents.size(), userId);
        return agents;
    }


    public void deleteAllAgents() {

        logger.warn("Deleting all registered agents.");

        agentRepository.deleteAll();

        logger.info("All agents deleted successfully.");
    }

    public Optional<Agent> findBySecureToken(String secureToken) {

        return agentRepository.findBySecureToken(secureToken);

    }

    public AgentResponseDto findByPublicId(String publicId) {


        logger.info("Fetching agent with publicId={}", publicId);

        Agent agent = agentRepository.findByPublicId(publicId).orElseThrow(() -> {

            logger.warn("Agent not found. publicId={}", publicId);

            return new ResourceNotFoundException("Agent not found");
        });

        logger.info("Successfully retrieved agent. publicId={}", publicId);

        return mapToAgentResponseDto(agent);

    }

    public void deleteAgent(String id) {
        logger.warn("Deleting agent with publicId={}", id);

        agentRepository.delete(id);

        logger.info("Agent deleted successfully. publicId={}", id);
    }
}
