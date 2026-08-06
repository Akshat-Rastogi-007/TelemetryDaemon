package com.telemtry.telemetryserver.telemetry.application;

import com.telemtry.telemetryserver.common.exception.ResourceNotFoundException;
import com.telemtry.telemetryserver.telemetry.api.respsonse.TelemetryBatchResponse;
import com.telemtry.telemetryserver.telemetry.domain.model.TelemetryBatch;
import com.telemtry.telemetryserver.telemetry.domain.repository.LatestMetricsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class TelemetryDashboardService {

        private final LatestMetricsRepository latestMetricsRepository;
    private final ModelMapper modelMapper;

    public TelemetryDashboardService(LatestMetricsRepository latestMetricsRepository,
                                     ModelMapper modelMapper)
    {
        this.latestMetricsRepository = latestMetricsRepository;
        this.modelMapper = modelMapper;
    }



    public TelemetryBatchResponse getBatch(Long agentId) {

        TelemetryBatch telemetryBatch = latestMetricsRepository.findByAgentId(agentId).orElseThrow(
                () -> new ResourceNotFoundException("No Metric Batch Received from " + agentId)
        );

        return modelMapper.map(telemetryBatch,TelemetryBatchResponse.class);

    }
}
