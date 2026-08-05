package com.telemtry.telemetryserver.telemetry.application;

import com.telemtry.telemetryserver.agent.api.CurrentAgentProvider;
import com.telemtry.telemetryserver.common.exception.ResourceNotFoundException;
import com.telemtry.telemetryserver.telemetry.api.request.TelemetryBatchRequest;
import com.telemtry.telemetryserver.telemetry.api.respsonse.TelemetryBatchResponse;
import com.telemtry.telemetryserver.telemetry.domain.model.Metric;
import com.telemtry.telemetryserver.telemetry.domain.model.TelemetryBatch;
import com.telemtry.telemetryserver.telemetry.domain.repository.LatestMetricsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TelemetryServiceImpl implements TelemetryService {

    private final LatestMetricsRepository latestMetricsRepository;
    private final ModelMapper modelMapper;
    private final CurrentAgentProvider currentAgentProvider;

    public TelemetryServiceImpl(LatestMetricsRepository latestMetricsRepository, ModelMapper modelMapper,
                                @Qualifier("currentAgentProviderImpl")
                                CurrentAgentProvider currentAgentProvider) {
        this.latestMetricsRepository = latestMetricsRepository;
        this.modelMapper = modelMapper;
        this.currentAgentProvider = currentAgentProvider;
    }

    @Override
    public void submitTelemetry(TelemetryBatchRequest batchRequest) {

        System.out.println(batchRequest);
//        TelemetryBatch batch = modelMapper.map(batchRequest, TelemetryBatch.class);

        TelemetryBatch batch = mapToTelemetryBatch(batchRequest);

        Long agent_id = currentAgentProvider.currentAgent().getId();
        batch.setAgentId(agent_id);

        // hardcoding it
        latestMetricsRepository.save(agent_id,batch);


    }

    private TelemetryBatch mapToTelemetryBatch(TelemetryBatchRequest batchRequest) {

        TelemetryBatch batch = new TelemetryBatch();

        batch.setTimestamp(batchRequest.getTimestamp());

        List<Metric> metrics = batchRequest.getMetrics()
                .stream()
                .map(metricRequest -> {

                    Metric metric = modelMapper.map(metricRequest, Metric.class);
                    metric.setTelemetryBatch(batch);
                    return metric;
                })
                .toList();

        batch.setMetrics(metrics);

        return batch;
    }

    @Override
    public TelemetryBatchResponse getBatch(Long agentId) {

        TelemetryBatch telemetryBatch = latestMetricsRepository.findByAgentId(agentId).orElseThrow(
                () -> new ResourceNotFoundException("No Metric Batch Received from " + agentId)
        );

        return modelMapper.map(telemetryBatch,TelemetryBatchResponse.class);

    }
}
