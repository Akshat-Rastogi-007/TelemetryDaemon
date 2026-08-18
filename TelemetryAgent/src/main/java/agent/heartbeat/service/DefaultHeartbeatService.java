package agent.heartbeat.service;

import agent.connection.ConnectionStateManager;
import agent.heartbeat.schedulars.HeartbeatSchedular;
import agent.heartbeat.transport.HeartbeatTransport;

import java.time.Duration;

public class DefaultHeartbeatService implements HeartbeatService{

    private final HeartbeatSchedular heartbeatSchedular;
    private final HeartbeatTransport heartbeatTransport;
    private final Duration heartBeatInterval;
    private final ConnectionStateManager connectionStateManager;

    public DefaultHeartbeatService(HeartbeatSchedular heartbeatSchedular,
                                   HeartbeatTransport heartbeatTransport,
                                   Duration heartBeatInterval, ConnectionStateManager connectionStateManager) {
        this.heartbeatSchedular = heartbeatSchedular;
        this.heartbeatTransport = heartbeatTransport;
        this.heartBeatInterval = heartBeatInterval;
        this.connectionStateManager = connectionStateManager;
    }


    @Override
    public void start() {


        heartbeatSchedular.schedule(

                ()-> {
                    try {
                        heartbeatTransport.sendHeartbeat();
                        connectionStateManager.markConnected();
                    }
                    catch (Exception e){

                        connectionStateManager.markDisconnected();

                    }

                }
                ,heartBeatInterval

        );

    }


    @Override
    public void stop() {

        heartbeatSchedular.stop();
        connectionStateManager.markDisconnected();

    }
}
