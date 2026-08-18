package agent.telemetry.buffer;

public interface TransportBufferObservable {

    void add(TransportBufferObserver transportBufferObserver);

    void remove(TransportBufferObserver transportBufferObserver);

    void notifyAllObservers();

}
