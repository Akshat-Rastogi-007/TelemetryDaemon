package agent.collector;

import agent.collector.scheduler.CollectorScheduler;
import agent.reporter.Reporter;
import configuration.AgentConfig;

import java.util.List;


public class CollectorEngine {


    private final CollectorScheduler collectorScheduler;
    private final List<Reporter> reporters;

    public CollectorEngine(CollectorScheduler collectorScheduler, List<Reporter> reporters) {
        this.collectorScheduler = collectorScheduler;
        this.reporters = reporters;
    }


    public void start(){

        collectorScheduler.startScheduler();

    }



    public void scheduleCollector(AgentConfig config){
        collectorScheduler.scheduleCollectors(config,reporters);
    }


    public void stop(){


        collectorScheduler.stopScheduler();

    }


}
