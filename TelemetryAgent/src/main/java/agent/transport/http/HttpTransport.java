package agent.transport.http;

import agent.searialization.JacksonTelemetrySerializer;
import agent.telemetry.TelemetryBatch;
import agent.transport.TelemetryTransport;
import exceptions.TransportException;
import identity.service.IdentityService;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpTransport implements TelemetryTransport {

    private final HttpClient client;
    private final JacksonTelemetrySerializer serializer;
    private final URI uri;
    private final IdentityService identityService;


    public HttpTransport(HttpClient client, JacksonTelemetrySerializer serializer, URI uri, IdentityService identityService) {
        this.client = client;
        this.serializer = serializer;
        this.uri = uri;
        this.identityService = identityService;
    }


    @Override
    public void send(TelemetryBatch telemetryBatch) {

        System.out.println("===== SENDING TELEMETRY =====");
        System.out.println("Destination : " + uri);
        System.out.println("Timestamp   : " + telemetryBatch.getTimestamp());

        String serialized = serializer.serialize(telemetryBatch);

        System.out.println("Payload:");
        System.out.println(serialized);

        String agentToken = identityService.getIdentity().getAgentToken();
        HttpRequest request = buildRequest(serialized, uri,agentToken);

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
            e.printStackTrace();
            Thread.currentThread().interrupt();
            throw new TransportException("HTTP request interrupted.", e);
        }
    }
    private HttpRequest buildRequest(String serialized, URI uri, String agentToken) {
        System.out.println("Bearer " + agentToken);
        return  HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + agentToken)
                .POST(HttpRequest.BodyPublishers.ofString(serialized))
                .uri(uri)
                .build();
    }
}
