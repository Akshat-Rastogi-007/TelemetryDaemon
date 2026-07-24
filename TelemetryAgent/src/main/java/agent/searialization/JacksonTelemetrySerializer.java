package agent.searialization;

import agent.telemetry.TelemetryBatch;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import exceptions.SerializationException;

public class JacksonTelemetrySerializer {


    private final ObjectMapper objectMapper;

    public JacksonTelemetrySerializer() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
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
