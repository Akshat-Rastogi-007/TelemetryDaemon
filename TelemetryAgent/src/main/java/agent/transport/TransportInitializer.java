package agent.transport;

import agent.connection.ConnectionStateManager;
import agent.telemetry.buffer.DefaultTransportBuffer;
import agent.telemetry.buffer.TransportBuffer;
import agent.telemetry.dispatcher.TelemetryDispatcher;
import agent.telemetry.flush.TelemetryFlusher;
import agent.telemetry.pipeline.TransportBufferPipeline;
import agent.telemetry.policy.BufferPolicy;
import agent.telemetry.policy.DefaultBufferPolicy;
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

        TransportBuffer defaultTransportBuffer = new DefaultTransportBuffer();


        BufferPolicy defaultBufferPolicy = new DefaultBufferPolicy(10);

        TelemetryFlusher telemetryFlusher = new TelemetryFlusher(defaultTransportBuffer, transport);

        stateManager.addObserver(telemetryFlusher);

        TransportBufferPipeline transportBufferPipeline = new TransportBufferPipeline(
                defaultTransportBuffer,defaultBufferPolicy
        );

        defaultTransportBuffer.add(telemetryFlusher);

        return new TelemetryDispatcher(stateManager,transport,transportBufferPipeline);
    }

}
