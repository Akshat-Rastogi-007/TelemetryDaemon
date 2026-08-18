package agent.connection;

import java.util.ArrayList;
import java.util.List;

public class ConnectionStateManager implements ConnectionStateObservable {

    private final List<ConnectionStateObserver> observers = new ArrayList<>();

    private ConnectionState state =
            ConnectionState.DISCONNECTED;



    public void markConnected() {

        state = ConnectionState.CONNECTED;
        notifyObservers();

    }

    public void markDisconnected(){

        state = ConnectionState.DISCONNECTED;

    }

    public ConnectionState getState(){

        return state;

    }

    @Override
    public void addObserver(ConnectionStateObserver connectionStateObserver) {

        observers.add(connectionStateObserver);

    }


    @Override
    public void removeObserver(ConnectionStateObserver connectionStateObserver) {

        observers.remove(connectionStateObserver);

    }

    @Override
    public void notifyObservers() {

        observers.forEach(

                (observers -> observers.onStateChanged(getState()))


        );

    }

}
