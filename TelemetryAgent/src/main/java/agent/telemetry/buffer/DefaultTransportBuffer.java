package agent.telemetry.buffer;

import agent.telemetry.TelemetryBatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DefaultTransportBuffer implements TransportBuffer{


    private final Queue<TelemetryBatch> queue =
            new ConcurrentLinkedQueue<>();

    private final List<TransportBufferObserver> observers = new ArrayList<>();


    @Override
    public void printBuffer() {
        System.out.println(queue);
    }

    @Override
    public void add(TelemetryBatch telemetryBatch) {

        queue.add(telemetryBatch);

    }

    @Override
    public List<TelemetryBatch> getPending() {

        return new ArrayList<>(queue);

    }

    @Override
    public int getSize() {

        return queue.size();

    }

    @Override
    public void remove(List<TelemetryBatch> batches) {
        queue.removeAll(batches);

    }

    @Override
    public void remove(TelemetryBatch batch) {
        queue.remove(batch);
    }

    @Override
    public TelemetryBatch removeOldest() {
        return queue.poll();

    }


    @Override
    public void add(TransportBufferObserver transportBufferObserver) {

        observers.add(transportBufferObserver);

    }

    @Override
    public void remove(TransportBufferObserver transportBufferObserver) {

        observers.remove(transportBufferObserver);
    }

    @Override
    public void notifyAllObservers() {

        observers.forEach(

                TransportBufferObserver::onBufferFull

        );


    }
}
