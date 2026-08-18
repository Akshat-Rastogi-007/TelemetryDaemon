package agent.telemetry.buffer;

import agent.telemetry.TelemetryBatch;

import java.util.List;

public interface TransportBuffer extends TransportBufferObservable {


    void printBuffer();

    void add(TelemetryBatch telemetryBatch);

    List<TelemetryBatch> getPending();

    int getSize();

    void remove(List<TelemetryBatch> batch);

    void remove(TelemetryBatch batch);

    TelemetryBatch removeOldest();


}
