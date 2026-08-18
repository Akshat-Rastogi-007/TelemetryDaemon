package com.telemtry.telemetryserver.agent.application;

import com.telemtry.telemetryserver.agent.api.CurrentAgentProvider;
import com.telemtry.telemetryserver.agent.domain.model.Agent;
import com.telemtry.telemetryserver.agent.domain.model.AgentStatus;
import com.telemtry.telemetryserver.agent.domain.repository.AgentRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AgentConfigurationService {

    private final CurrentAgentProvider currentAgentProvider;
    private final AgentRepository agentRepository;


    public AgentConfigurationService(CurrentAgentProvider currentAgentProvider,
                                     @Qualifier("jpaAgentRepository")
                                     AgentRepository agentRepository) {
        this.currentAgentProvider = currentAgentProvider;
        this.agentRepository = agentRepository;
    }


    public void agentHeartbeatCheck(){

        Agent agent = currentAgentProvider.currentAgent();


        agent.setStatus(AgentStatus.ONLINE);
        agent.setLastHeartBeat(LocalDateTime.now());

        agentRepository.save(agent);

    }
}
