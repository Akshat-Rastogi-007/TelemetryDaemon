package agent.schedular;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DefaultSchedular implements Scheduler {

    public DefaultSchedular() {

        System.out.println("CREATING SCHEDULAR");
    }

    private final ScheduledExecutorService executor =
            Executors.newSingleThreadScheduledExecutor();

    @Override
    public void start() {

        System.out.println("*****Starting SCHEDULER*****");

        // start the collectors and stuff
        // not needed right now

    }

    @Override
    public void stop() {

        // stop the collectors

        System.out.println("**Stopping the Executor**");
        executor.shutdown();
        System.out.println("**Executor Stopped**");

    }

    @Override
    public void schedule(Runnable task, Duration interval) {

        // task which we need to schedule

        executor.scheduleAtFixedRate(task,0,interval.getSeconds(), TimeUnit.SECONDS);

    }
}
