package agent.searialization;

import agent.telemetry.TelemetryBatch;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.SerializationException;

public class JacksonTelemetrySerializer {


    private final ObjectMapper objectMapper;

    public JacksonTelemetrySerializer() {
        this.objectMapper = new ObjectMapper();
    }

    public String serialize(TelemetryBatch batch) {
        try {
            return objectMapper.writeValueAsString(batch);
        } catch (JsonProcessingException e) {
            throw new SerializationException(
                    "Failed to serialize TelemetryBatch",   e);
        }
    }


}
