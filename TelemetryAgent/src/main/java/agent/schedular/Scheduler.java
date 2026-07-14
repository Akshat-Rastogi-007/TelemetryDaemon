package agent.schedular;

import java.time.Duration;

public interface Scheduler {


    void start();

    void stop();

    void schedule(Runnable task, Duration interval);

}
