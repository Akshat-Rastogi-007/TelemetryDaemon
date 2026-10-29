package com.telemtry.telemetryserver.telemetry.application;

import com.telemtry.telemetryserver.agent.domain.model.Agent;
import com.telemtry.telemetryserver.agent.domain.repository.AgentRepository;
import com.telemtry.telemetryserver.common.exception.ResourceNotFoundException;
import com.telemtry.telemetryserver.telemetry.api.respsonse.TelemetryBatchResponse;
import com.telemtry.telemetryserver.telemetry.domain.model.TelemetryBatch;
import com.telemtry.telemetryserver.telemetry.domain.repository.LatestMetricsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class TelemetryDashboardService {

    private final LatestMetricsRepository latestMetricsRepository;
    private final AgentRepository agentRepository;
    private final ModelMapper modelMapper;

    public TelemetryDashboardService(LatestMetricsRepository latestMetricsRepository,
                                     @Qualifier("jpaAgentRepository") AgentRepository agentRepository,
                                     ModelMapper modelMapper)
    {
        this.latestMetricsRepository = latestMetricsRepository;
        this.agentRepository = agentRepository;
        this.modelMapper = modelMapper;
    }



    public TelemetryBatchResponse getBatch(String publicId) {

        // Telemetry is stored under the agent's internal id; resolve it from the public id
        Agent agent = agentRepository.findByPublicId(publicId).orElseThrow(
                () -> new ResourceNotFoundException("Agent not found: " + publicId)
        );

        TelemetryBatch telemetryBatch = latestMetricsRepository.findByAgentId(agent.getId()).orElseThrow(
                () -> new ResourceNotFoundException("No Metric Batch Received from " + publicId)
        );

        return modelMapper.map(telemetryBatch,TelemetryBatchResponse.class);

    }
}
