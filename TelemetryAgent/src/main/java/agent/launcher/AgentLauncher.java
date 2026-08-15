package agent.launcher;

import agent.collector.Collector;
import agent.collector.CollectorEngine;
import agent.collector.CollectorInitializer;
import agent.collector.CollectorRegister;
import agent.collector.cpu.CpuCollector;
import agent.collector.disk.DiskCollector;
import agent.collector.manager.CollectorManager;
import agent.collector.memory.MemoryCollector;
import agent.collector.scheduler.CollectorScheduler;
import agent.connection.ConnectionStateManager;
import agent.heartbeat.HeartbeatInitializer;
import agent.heartbeat.service.HeartbeatService;
import agent.lifecycle.Agent;
import agent.lifecycle.DefaultAgent;
import agent.platform.Platform;
import agent.platform.PlatformFactory;
import agent.reporter.Reporter;
import agent.reporter.impl.FileReporter;
import agent.reporter.impl.HttpReporter;
import agent.schedular.DefaultSchedular;
import agent.schedular.Scheduler;
import agent.transport.TelemetryTransport;
import agent.transport.TransportFactory;
import agent.transport.TransportInitializer;
import agent.transport.dispatcher.TelemetryDispatcher;
import configuration.AgentConfig;
import identity.bootstrap.IdentityBootstrap;
import identity.service.IdentityService;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgentLauncher {

    public Agent launch(AgentConfig config){

        Scheduler scheduler = new DefaultSchedular();


        ConnectionStateManager connectionStateManager = new ConnectionStateManager();

        HeartbeatService heartbeatService = new HeartbeatInitializer(config,connectionStateManager).initialize();

        TelemetryDispatcher telemetryDispatcher = new TransportInitializer(config).initialize(connectionStateManager);

        List<Reporter> reporters = List.of(
//                new ConsoleReporter(),
                new FileReporter(Paths.get("logs")),
                new HttpReporter(telemetryDispatcher)
        );


        CollectorEngine collectorEngine = new CollectorInitializer(reporters).initialize(scheduler);

        Agent agent = new DefaultAgent(config,collectorEngine,heartbeatService);

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
