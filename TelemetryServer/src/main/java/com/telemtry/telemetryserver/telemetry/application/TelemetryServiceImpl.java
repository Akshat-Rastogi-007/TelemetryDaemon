package com.telemtry.telemetryserver.telemetry.application;

import com.telemtry.telemetryserver.common.exception.ResourceNotFoundException;
import com.telemtry.telemetryserver.telemetry.api.request.TelemetryBatchRequest;
import com.telemtry.telemetryserver.telemetry.api.respsonse.TelemetryBatchResponse;
import com.telemtry.telemetryserver.telemetry.domain.model.Metric;
import com.telemtry.telemetryserver.telemetry.domain.model.TelemetryBatch;
import com.telemtry.telemetryserver.telemetry.domain.repository.LatestMetricsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TelemetryServiceImpl implements TelemetryService {

    private final LatestMetricsRepository latestMetricsRepository;
    private final ModelMapper modelMapper;

    public TelemetryServiceImpl(LatestMetricsRepository latestMetricsRepository, ModelMapper modelMapper) {
        this.latestMetricsRepository = latestMetricsRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void submitTelemetry(TelemetryBatchRequest batchRequest) {

        System.out.println(batchRequest);
//        TelemetryBatch batch = modelMapper.map(batchRequest, TelemetryBatch.class);

        TelemetryBatch batch = mapToTelemetryBatch(batchRequest);


        // hardcoding it
        latestMetricsRepository.save("agent-1",batch);


    }

    private TelemetryBatch mapToTelemetryBatch(TelemetryBatchRequest batchRequest) {

        TelemetryBatch batch = new TelemetryBatch();

        batch.setCollectorId(batchRequest.getCollectorId());
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
    public TelemetryBatchResponse getBatch(String agentId) {

        TelemetryBatch telemetryBatch = latestMetricsRepository.findByAgentId(agentId).orElseThrow(
                () -> new ResourceNotFoundException("No Metric Batch Received from " + agentId)
        );

        return modelMapper.map(telemetryBatch,TelemetryBatchResponse.class);

    }
}
