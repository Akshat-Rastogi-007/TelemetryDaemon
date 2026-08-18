package agent.connection;

public interface ConnectionStateObservable {

    void addObserver(ConnectionStateObserver connectionStateObserver);

    void removeObserver(ConnectionStateObserver connectionStateObserver);

    void notifyObservers();

}
