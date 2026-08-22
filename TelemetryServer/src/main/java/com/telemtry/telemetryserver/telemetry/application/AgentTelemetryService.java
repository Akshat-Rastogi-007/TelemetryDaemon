package com.telemtry.telemetryserver.telemetry.application;

import com.telemtry.telemetryserver.agent.api.CurrentAgentProvider;
import com.telemtry.telemetryserver.telemetry.api.request.TelemetryBatchRequest;
import com.telemtry.telemetryserver.telemetry.api.respsonse.CollectorMetricsResponse;
import com.telemtry.telemetryserver.telemetry.api.respsonse.MetricResponse;
import com.telemtry.telemetryserver.telemetry.api.respsonse.TelemetryBatchResponse;
import com.telemtry.telemetryserver.telemetry.domain.model.CollectorMetrics;
import com.telemtry.telemetryserver.telemetry.domain.model.Metric;
import com.telemtry.telemetryserver.telemetry.domain.model.TelemetryBatch;
import com.telemtry.telemetryserver.telemetry.domain.repository.LatestMetricsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class AgentTelemetryService {

    private final ModelMapper modelMapper;
    private final LatestMetricsRepository latestMetricsRepository;
    private final CurrentAgentProvider currentAgentProvider;

    public AgentTelemetryService(ModelMapper modelMapper,
                                 LatestMetricsRepository latestMetricsRepository,
                                 @Qualifier("currentAgentProviderImpl")
                                 CurrentAgentProvider currentAgentProvider) {
        this.modelMapper = modelMapper;
        this.latestMetricsRepository = latestMetricsRepository;
        this.currentAgentProvider = currentAgentProvider;
    }


    public void submitTelemetry(TelemetryBatchRequest batchRequest) {

//        System.out.println(batchRequest);
//        TelemetryBatch batch = modelMapper.map(batchRequest, TelemetryBatch.class);

        TelemetryBatch batch = mapToTelemetryBatch(batchRequest);

        Long agent_id = currentAgentProvider.currentAgent().getId();
        batch.setAgentId(agent_id);
        
        TelemetryBatch savedBatch = latestMetricsRepository.save(agent_id, batch);

        System.out.println(mapToResponseDto(savedBatch));


    }

    private TelemetryBatch mapToTelemetryBatch(TelemetryBatchRequest batchRequest) { {

            TelemetryBatch batch = new TelemetryBatch();

            batch.setTimestamp(batchRequest.getTimestamp());

            List<CollectorMetrics> collectorMetricsList =
                    batchRequest.getMetricMap()
                            .entrySet()
                            .stream()
                            .map(entry -> {

                                CollectorMetrics collectorMetrics =
                                        new CollectorMetrics();

                                collectorMetrics.setCollectorId(entry.getKey());
                                collectorMetrics.setTelemetryBatch(batch);

                                List<Metric> metrics =
                                        entry.getValue()
                                                .stream()
                                                .map(metricRequest -> {

                                                    Metric metric =
                                                            modelMapper.map(
                                                                    metricRequest,
                                                                    Metric.class
                                                            );

                                                    metric.setCollectorMetrics(
                                                            collectorMetrics
                                                    );

                                                    return metric;
                                                })
                                                .toList();

                                collectorMetrics.setMetrics(metrics);

                                return collectorMetrics;
                            })
                            .toList();

            batch.setCollectorMetrics(collectorMetricsList);

            return batch;
        }
    }

    private TelemetryBatchResponse mapToResponseDto(TelemetryBatch batch) {

        TelemetryBatchResponse response = new TelemetryBatchResponse();

        response.setTimestamp(batch.getTimestamp());
        response.setAgentId(batch.getAgentId());

        List<CollectorMetricsResponse> collectorResponses =
                batch.getCollectorMetrics()
                        .stream()
                        .map(collectorMetrics -> {

                            CollectorMetricsResponse collectorResponse =
                                    new CollectorMetricsResponse();

                            collectorResponse.setCollectorId(
                                    collectorMetrics.getCollectorId()
                            );

                            List<MetricResponse> metrics =
                                    collectorMetrics.getMetrics()
                                            .stream()
                                            .map(metric -> {

                                                MetricResponse metricResponse =
                                                        new MetricResponse();

                                                metricResponse.setName(metric.getName());
                                                metricResponse.setValue(metric.getValue());
                                                metricResponse.setUnit(metric.getUnit());
                                                metricResponse.setTimestamp(metric.getTimestamp());
                                                metricResponse.setAttributes(
                                                        new HashMap<>(metric.getAttributes())
                                                );

                                                return metricResponse;
                                            })
                                            .toList();

                            collectorResponse.setMetrics(metrics);

                            return collectorResponse;
                        })
                        .toList();

        response.setCollectorMetrics(collectorResponses);

        return response;
    }
}
