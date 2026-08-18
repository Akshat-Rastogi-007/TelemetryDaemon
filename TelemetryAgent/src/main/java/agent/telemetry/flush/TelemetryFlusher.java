package agent.telemetry.flush;

import agent.connection.ConnectionState;
import agent.connection.ConnectionStateObserver;
import agent.telemetry.TelemetryBatch;
import agent.telemetry.buffer.TransportBuffer;
import agent.telemetry.buffer.TransportBufferObserver;
import agent.transport.TelemetryTransport;
import exceptions.TransportException;

import java.util.List;

public class TelemetryFlusher implements ConnectionStateObserver, TransportBufferObserver {

    private final TransportBuffer transportBuffer;
    private final TelemetryTransport telemetryTransport;

    public TelemetryFlusher(TransportBuffer transportBuffer, TelemetryTransport telemetryTransport) {
        this.transportBuffer = transportBuffer;
        this.telemetryTransport = telemetryTransport;
    }

    @Override
    public void onStateChanged(ConnectionState state) {

        List<TelemetryBatch> pendingBatches = transportBuffer.getPending();

        for ( TelemetryBatch batch : pendingBatches) {

            try {
                telemetryTransport.send(batch);
            }
            catch (TransportException e){

                System.out.println("Not sending pending buffer");
                break;
            }
            transportBuffer.remove(batch);
        }


    }

    @Override
    public void onBufferFull() {

        TelemetryBatch removed = transportBuffer.removeOldest();

        if (removed != null) {
            System.out.println(
                    "Buffer full. Removed oldest telemetry batch: "
                            + removed.getTimestamp()
            );
        }

    }
}
