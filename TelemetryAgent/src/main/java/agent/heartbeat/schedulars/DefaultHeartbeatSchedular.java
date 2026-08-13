package agent.heartbeat.schedulars;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DefaultHeartbeatSchedular implements HeartbeatSchedular{


    private final ScheduledExecutorService executor =
            Executors.newSingleThreadScheduledExecutor();

    @Override
    public void schedule(
            Runnable task,
            Duration interval
    ) {

        executor.scheduleAtFixedRate(
                task,
                0,
                interval.toSeconds(),
                TimeUnit.SECONDS
        );

    }


    @Override
    public void stop() {

        executor.shutdown();

    }
}
