package agent.collector.scheduler;

import agent.collector.CollectorRegister;
import agent.collector.manager.CollectorManager;
import agent.schedular.Scheduler;
import configuration.AgentConfig;

public class CollectorScheduler {

    private final CollectorManager collectorManager;
    private final Scheduler scheduler;


    public CollectorScheduler(CollectorManager collectorManager, Scheduler scheduler) {
        this.collectorManager = collectorManager;
        this.scheduler = scheduler;
    }


    public void startScheduler(){

        scheduler.start();

    }


    public void scheduleCollectors(AgentConfig agentConfig){

        System.out.println("Scheduling Scheduler");
        for (CollectorRegister register : collectorManager.getCollectors()){


            if (register.isEnable()) {
                System.out.println(register.getCollector().getId());
                scheduler.schedule(register.getCollector(), agentConfig.getHeartbeatDuration());
            }
        }

    }

    public void stopScheduler(){
        scheduler.stop();
    }
}
