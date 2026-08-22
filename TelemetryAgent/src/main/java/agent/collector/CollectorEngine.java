package agent.collector;

import agent.collector.scheduler.CollectorScheduler;
import agent.reporter.Reporter;
import agent.transport.TelemetryTransport;
import configuration.AgentConfig;

import java.util.List;


public class CollectorEngine {


    private final CollectorScheduler collectorScheduler;

    public CollectorEngine(CollectorScheduler collectorScheduler) {
        this.collectorScheduler = collectorScheduler;
    }


    public void start(){

        collectorScheduler.startScheduler();

    }



    public void scheduleCollector(AgentConfig config){
        collectorScheduler.scheduleCollectors(config);
    }


    public void stop(){


        collectorScheduler.stopScheduler();

    }


}
