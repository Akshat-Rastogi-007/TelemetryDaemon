package agent.telemetry.dispatcher;

import agent.connection.ConnectionState;
import agent.connection.ConnectionStateManager;
import agent.telemetry.TelemetryBatch;
import agent.telemetry.pipeline.TransportBufferPipeline;
import agent.transport.TelemetryTransport;
import exceptions.TransportException;

public class TelemetryDispatcher {

    private final ConnectionStateManager connectionStateManager;
    private final TelemetryTransport transport;
    private final TransportBufferPipeline transportBufferPipeline;

    public TelemetryDispatcher(ConnectionStateManager connectionStateManager, TelemetryTransport transport, TransportBufferPipeline transportBufferPipeline) {
        this.connectionStateManager = connectionStateManager;
        this.transport = transport;
        this.transportBufferPipeline = transportBufferPipeline;
    }


    public void dispatch(TelemetryBatch batch){

        if (connectionStateManager.getState() == ConnectionState.CONNECTED){

            try {

                transport.send(batch);

            }
            catch(TransportException e){

                transportBufferPipeline.store(batch);

                connectionStateManager.markDisconnected();

            }
        }
        else{

            transportBufferPipeline.store(batch);

        }

    }
}
