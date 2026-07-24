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

        System.out.println("===== SENDING TELEMETRY =====");
        System.out.println("Destination : " + uri);
        System.out.println("Timestamp   : " + telemetryBatch.getTimestamp());
        System.out.println("Metrics     : " + telemetryBatch.getMetrics().size());

        String serialized = serializer.serialize(telemetryBatch);

        System.out.println("Payload:");
        System.out.println(serialized);

        HttpRequest request = buildRequest(serialized, uri);

        try {

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("HTTP Status : " + response.statusCode());

            if (!response.body().isBlank()) {
                System.out.println("Response:");
                System.out.println(response.body());
            }

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new TransportException(
                        "Failed to send telemetry. HTTP Status: " + response.statusCode());
            }

            System.out.println("Telemetry sent successfully.");

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
