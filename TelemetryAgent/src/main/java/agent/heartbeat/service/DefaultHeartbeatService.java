package agent.heartbeat.service;

import agent.heartbeat.schedulars.HeartbeatSchedular;
import agent.heartbeat.transport.HeartbeatTransport;

import java.time.Duration;

public class DefaultHeartbeatService implements HeartbeatService{

    private final HeartbeatSchedular heartbeatSchedular;
    private final HeartbeatTransport heartbeatTransport;
    private final Duration heartBeatInterval;

    public DefaultHeartbeatService(HeartbeatSchedular heartbeatSchedular, HeartbeatTransport heartbeatTransport, Duration heartBeatInterval) {
        this.heartbeatSchedular = heartbeatSchedular;
        this.heartbeatTransport = heartbeatTransport;
        this.heartBeatInterval = heartBeatInterval;
    }


    @Override
    public void start() {

        heartbeatSchedular.schedule(
                heartbeatTransport::sendHeartbeat,
                heartBeatInterval
        );

    }


    @Override
    public void stop() {

        heartbeatSchedular.stop();

    }
}
