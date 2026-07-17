package agent.schedular;

import agent.collector.Collector;
import agent.reporter.Reporter;

import java.time.Duration;
import java.util.List;

public interface Scheduler {


    void start();

    void stop();

    void schedule(Collector collector, List<Reporter> reporters, Duration interval);

}
