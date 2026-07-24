package agent.transport;

import agent.searialization.JacksonTelemetrySerializer;
import agent.transport.http.HttpTransport;
import configuration.AgentConfig;
import exceptions.ResourceNotFoundException;

import java.net.URI;
import java.net.http.HttpClient;
import java.nio.file.Path;

public class TransportFactory {

    private final AgentConfig config;

    public TransportFactory(AgentConfig config) {
        this.config = config;
    }

    public TelemetryTransport create(){

        String transportType = config.getTransportType();

        if (transportType.contains("http")){

            JacksonTelemetrySerializer jacksonTelemetrySerializer = new JacksonTelemetrySerializer();


            return new HttpTransport(HttpClient.newHttpClient(),jacksonTelemetrySerializer, URI.create(config.getServerUrl()+"/app/telemetry/submit"));
        }

        throw new ResourceNotFoundException("Transport Type " + transportType +" is not supported yet");
    }
}
