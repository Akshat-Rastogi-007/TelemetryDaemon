package com.telemtry.telemetryserver.agent.domain.service;

import com.telemtry.telemetryserver.agent.domain.repository.AgentRepository;
import org.springframework.stereotype.Service;

@Service
public class AgentService {

    private final AgentRepository agentRepository;


    public AgentService(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }


}
