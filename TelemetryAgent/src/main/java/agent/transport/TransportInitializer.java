package agent.transport;

import agent.connection.ConnectionStateManager;
import agent.transport.buffer.TransportBuffer;
import agent.transport.dispatcher.TelemetryDispatcher;
import configuration.AgentConfig;
import identity.bootstrap.IdentityBootstrap;
import identity.service.IdentityService;

public class TransportInitializer {


    private final AgentConfig agentConfig;

    public TransportInitializer(AgentConfig agentConfig) {
        this.agentConfig = agentConfig;
    }

    public TelemetryDispatcher initialize(ConnectionStateManager stateManager) {


        IdentityService identityService = IdentityBootstrap.initialize();


        TelemetryTransport transport = new TransportFactory(agentConfig, identityService).create();

        TransportBuffer transportBuffer = new TransportBuffer();

        return new TelemetryDispatcher(stateManager,transport,transportBuffer);
    }

}
