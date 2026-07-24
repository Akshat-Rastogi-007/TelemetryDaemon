package com.telemtry.telemetryserver.telemetry.application;

import com.telemtry.telemetryserver.telemetry.api.request.TelemetryBatchRequest;
import com.telemtry.telemetryserver.telemetry.api.respsonse.TelemetryBatchResponse;
import org.springframework.stereotype.Component;

@Component
public interface TelemetryService {


    void submitTelemetry(TelemetryBatchRequest batchRequest);

    TelemetryBatchResponse getBatch(String agentId);

}
