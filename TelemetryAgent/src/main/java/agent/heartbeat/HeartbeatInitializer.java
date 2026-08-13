package agent.heartbeat;


import agent.heartbeat.schedulars.DefaultHeartbeatSchedular;
import agent.heartbeat.schedulars.HeartbeatSchedular;
import agent.heartbeat.service.DefaultHeartbeatService;
import agent.heartbeat.service.HeartbeatService;
import agent.heartbeat.transport.HeartbeatTransport;
import agent.heartbeat.transport.HttpHeartbeatTransport;
import configuration.AgentConfig;
import identity.bootstrap.IdentityBootstrap;
import identity.service.IdentityService;

import java.net.URI;
import java.net.http.HttpClient;

public class HeartbeatInitializer {


    private final AgentConfig config;
    private final IdentityService identityService;

    public HeartbeatInitializer(AgentConfig config) {
        this.identityService = IdentityBootstrap.initialize();
        this.config = config;
    }

    public HeartbeatService initialize() {

        HeartbeatSchedular schedular =
                new DefaultHeartbeatSchedular();


        HeartbeatTransport transport =
                new HttpHeartbeatTransport(
                        HttpClient.newHttpClient(),
                        URI.create(
                                config.getServerUrl()
                                        + "/app/agent/heartbeat/"
                        ),
                        identityService.getIdentity().getAgentToken()
                );


        return new DefaultHeartbeatService(
                schedular,
                transport,
                config.getHeartbeatDuration()
        );

    }


}
