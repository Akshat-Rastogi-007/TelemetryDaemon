package agent.transport.dispatcher;

import agent.connection.ConnectionState;
import agent.connection.ConnectionStateManager;
import agent.telemetry.TelemetryBatch;
import agent.transport.TelemetryTransport;
import agent.transport.buffer.TransportBuffer;
import exceptions.TransportException;

public class TelemetryDispatcher {

    private final ConnectionStateManager connectionStateManager;
    private final TelemetryTransport transport;
    private final TransportBuffer transportBuffer;

    public TelemetryDispatcher(ConnectionStateManager connectionStateManager, TelemetryTransport transport, TransportBuffer transportBuffer) {
        this.connectionStateManager = connectionStateManager;
        this.transport = transport;
        this.transportBuffer = transportBuffer;
    }


    public void dispatch(TelemetryBatch batch){

        if (connectionStateManager.getState() == ConnectionState.CONNECTED){

            try {

                transport.send(batch);

            }
            catch(TransportException e){

                transportBuffer.add(batch);

                connectionStateManager.markDisconnected();

            }
        }
        else{

            transportBuffer.add(batch);

        }

    }
}
