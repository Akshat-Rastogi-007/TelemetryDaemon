package agent.launcher;

import agent.collector.CollectorEngine;
import agent.collector.CollectorRegister;
import agent.collector.CpuCollector;
import agent.collector.manager.CollectorManager;
import agent.collector.scheduler.CollectorScheduler;
import agent.lifecycle.Agent;
import agent.lifecycle.DefaultAgent;
import agent.schedular.DefaultSchedular;
import agent.schedular.Scheduler;
import configuration.AgentConfig;

import java.util.HashMap;
import java.util.Map;

public class AgentLauncher {

    public Agent launch(AgentConfig config){

        System.out.println("*****LAUNCHING AGENT*****");
        Scheduler scheduler = new DefaultSchedular();


        Map<String, CollectorRegister> collectorMap = new HashMap<>();

        CollectorManager manager = new CollectorManager(collectorMap);

        manager.registerCollector(new CpuCollector());

        CollectorScheduler collectorScheduler = new CollectorScheduler(manager, scheduler);

        CollectorEngine collectorEngine = new CollectorEngine(collectorScheduler);


        Agent agent = new DefaultAgent(config,collectorEngine);


        registerShutdownHook(agent);

        agent.start();

        return agent;

    }

    private static void registerShutdownHook(Agent agent) {

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {

            System.err.println("===== SHUTDOWN HOOK =====");

            agent.stop();

            System.err.println("===== STOP FINISHED =====");

        }));
    }


}
