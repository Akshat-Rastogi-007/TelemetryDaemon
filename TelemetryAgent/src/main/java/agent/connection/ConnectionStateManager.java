package agent.connection;

public class ConnectionStateManager {

    private ConnectionState state =
            ConnectionState.DISCONNECTED;


    public void markConnected() {

        state = ConnectionState.CONNECTED;

    }

    public void markDisconnected(){

        state = ConnectionState.DISCONNECTED;

    }

    public ConnectionState getState(){

        return state;

    }

}
