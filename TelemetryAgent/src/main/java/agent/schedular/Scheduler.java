package agent.schedular;

import agent.collector.Collector;

import java.time.Duration;

public interface Scheduler {


    void start();

    void stop();

    void schedule(Collector collector, Duration interval);

}
