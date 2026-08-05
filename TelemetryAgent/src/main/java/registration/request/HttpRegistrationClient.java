package registration.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import configuration.AgentConfig;
import exceptions.RegistrationException;
import registration.client.RegistrationClient;
import registration.response.RegistrationResponse;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpRegistrationClient implements RegistrationClient {


    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    public HttpRegistrationClient(ObjectMapper objectMapper, HttpClient httpClient) {
        this.objectMapper = objectMapper;
        this.httpClient = httpClient;
    }

    @Override
    public RegistrationResponse register(RegistrationRequest request, String patToken, AgentConfig config) {

        HttpRequest httpRequest =
                buildRequest(config, request, patToken);

        HttpResponse<String> response =
                sendRequest(httpRequest);


        if (response.statusCode() != 201) {

            throw new RegistrationException(
                    "Registration failed. Status Code : "
                            + response.statusCode() + "    " + response.body()
            );

        }

        return parseResponse(response);

    }

    private HttpRequest buildRequest(AgentConfig config, RegistrationRequest request, String personalAccessToken) {
        try {

            String body = objectMapper.writeValueAsString(request);

            return HttpRequest.newBuilder()
                    .uri(URI.create(config.getServerUrl() + "/app/agent/register-agent/"))
                    .header("Authorization", "Bearer " + personalAccessToken)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

        } catch (JsonProcessingException exception) {
            throw new RegistrationException(
                    "Failed to serialize registration request.",
                    exception
            );
        }

    }

    private HttpResponse<String> sendRequest(HttpRequest request) {

        try {
            return
                    httpClient.send(request,
                            HttpResponse.BodyHandlers.ofString()
                    );
        } catch (IOException | InterruptedException exception) {
            throw new RegistrationException(
                    "Failed to communicate with TelemetryServer.",
                    exception
            );
        }
    }

    private RegistrationResponse parseResponse(
            HttpResponse<String> response
    ) {

        try {

            JsonNode root = objectMapper.readTree(response.body());

            JsonNode data = root.get("data");

            RegistrationResponse registrationResponse = new RegistrationResponse();

            registrationResponse.setAgentId(
                    data.get("agentId").asText()
            );

            registrationResponse.setAgentToken(
                    data.get("secureKey").asText()
            );

            return registrationResponse;

        } catch (IOException exception) {

            throw new RegistrationException(
                    "Failed to parse server response.",
                    exception
            );

        }
    }
}