package com.telemtry.telemetryserver.agent.api;

import com.telemtry.telemetryserver.agent.domain.model.Agent;

public interface CurrentAgentProvider {

    Agent currentAgent();

}
