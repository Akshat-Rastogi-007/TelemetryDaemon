package agent.collector;

import agent.collector.scheduler.CollectorScheduler;
import configuration.AgentConfig;


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
