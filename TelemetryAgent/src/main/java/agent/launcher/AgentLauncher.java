package agent.launcher;

import agent.collector.Collector;
import agent.collector.CollectorEngine;
import agent.collector.CollectorRegister;
import agent.collector.cpu.CpuCollector;
import agent.collector.disk.DiskCollector;
import agent.collector.manager.CollectorManager;
import agent.collector.memory.MemoryCollector;
import agent.collector.scheduler.CollectorScheduler;
import agent.lifecycle.Agent;
import agent.lifecycle.DefaultAgent;
import agent.platform.Platform;
import agent.platform.PlatformFactory;
import agent.reporter.Reporter;
import agent.reporter.impl.ConsoleReporter;
import agent.reporter.impl.FileReporter;
import agent.reporter.impl.HttpReporter;
import agent.schedular.DefaultSchedular;
import agent.schedular.Scheduler;
import agent.transport.TelemetryTransport;
import agent.transport.TransportFactory;
import configuration.AgentConfig;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgentLauncher {

    public Agent launch(AgentConfig config){

        System.out.println("*****INSIDE AGENT LAUNCHER*****");

        System.out.println("*****IMPLEMENTED Default SCHEDULAR*****");
        Scheduler scheduler = new DefaultSchedular();

        TransportFactory transportFactory = new TransportFactory(config);

        TelemetryTransport transport = transportFactory.create();

        Map<String, CollectorRegister> collectorMap = new HashMap<>();

        CollectorManager manager = new CollectorManager(collectorMap);

        Platform platform = new PlatformFactory().create();

        System.out.printf("*****Platform Received -> %s ***** \n ", platform.getId());

        List<Collector> collectors = List.of(
                new CpuCollector(platform.cpu()),
                new DiskCollector(platform.disk()),
                new MemoryCollector(platform.memory())
        );

        manager.registerCollector(collectors);

        CollectorScheduler collectorScheduler = new CollectorScheduler(manager, scheduler);


        List<Reporter> reporters = List.of(
//                new ConsoleReporter(),
                new FileReporter(Paths.get("logs")),
                new HttpReporter(transport)
        );

        CollectorEngine collectorEngine = new CollectorEngine(collectorScheduler,reporters);

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
