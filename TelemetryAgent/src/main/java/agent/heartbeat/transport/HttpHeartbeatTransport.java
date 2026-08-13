package agent.heartbeat.transport;

import exceptions.TransportException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpHeartbeatTransport implements HeartbeatTransport{


    private final HttpClient client;
    private final URI uri;
    private final String agentToken;



    public HttpHeartbeatTransport(HttpClient client, URI uri, String agentToken) {
        this.client = client;
        this.uri = uri;
        this.agentToken = agentToken;
    }

    @Override
    public void sendHeartbeat() {

        System.out.println("===== SENDING HEARTBEAT =====");
        System.out.println("Destination : " + uri);


        HttpRequest request = buildRequest();


        try {

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );


            System.out.println(
                    "Heartbeat Status : " + response.statusCode()
            );


            if (response.statusCode() >= 200 &&
                    response.statusCode() < 300) {

                System.out.println(
                        "Heartbeat sent successfully."
                );

                return;
            }


            System.out.println(
                    "Heartbeat failed. Server returned : "
                            + response.statusCode()
            );


            throw new TransportException(
                    "Heartbeat failed with status: "
                            + response.statusCode()
            );


        } catch (IOException e) {

            System.out.println(
                    "I/O error while sending heartbeat."
            );

            throw new TransportException(
                    "Unable to send heartbeat.",
                    e
            );


        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            System.out.println(
                    "Heartbeat request interrupted."
            );

            throw new TransportException(
                    "Heartbeat interrupted.",
                    e
            );

        }

    }

    private HttpRequest buildRequest() {

        return HttpRequest.newBuilder()
                .uri(uri)
                .header(
                        "Content-Type",
                        "application/json"
                )
                .header(
                        "Accept",
                        "application/json"
                )
                .header(
                        "Authorization",
                        "Bearer " + agentToken
                )
                .PUT(
                        HttpRequest.BodyPublishers.noBody()
                )
                .build();

    }
}
