package agent.transport.http;

import agent.searialization.JacksonTelemetrySerializer;
import agent.telemetry.TelemetryBatch;
import agent.transport.TelemetryTransport;
import exceptions.TransportException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpTransport implements TelemetryTransport {

    private final HttpClient client;
    private final JacksonTelemetrySerializer serializer;
    private final URI uri;


    public HttpTransport(HttpClient client, JacksonTelemetrySerializer serializer, URI uri) {
        this.client = client;
        this.serializer = serializer;
        this.uri = uri;
    }

    @Override
    public void send(TelemetryBatch telemetryBatch) {


        String serialized = serializer.serialize(telemetryBatch);

        HttpRequest request = buildRequest(serialized,uri);

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200){
                System.out.println("**Server Received the data**");
            }

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new TransportException(
                        "Failed to send telemetry. HTTP Status: " + response.statusCode());
            }

        } catch (IOException e) {
            throw new TransportException("I/O error while sending telemetry.", e);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new TransportException("HTTP request interrupted.", e);
        }


    }

    private HttpRequest buildRequest(String serialized, URI uri) {
        return  HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(serialized))
                .uri(uri)
                .build();
    }
}
