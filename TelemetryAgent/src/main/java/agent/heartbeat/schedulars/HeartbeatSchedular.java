package agent.heartbeat.schedulars;

import agent.connection.ConnectionStateManager;

import java.time.Duration;

public interface HeartbeatSchedular {

    void schedule(
            Runnable task,
            Duration interval
    );

    void stop();
}
