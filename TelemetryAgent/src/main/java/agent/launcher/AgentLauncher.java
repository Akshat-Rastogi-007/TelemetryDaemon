package agent.launcher;

import agent.collector.CollectorEngine;
import agent.collector.CollectorInitializer;
import agent.connection.ConnectionStateManager;
import agent.heartbeat.HeartbeatInitializer;
import agent.heartbeat.service.HeartbeatService;
import agent.lifecycle.Agent;
import agent.lifecycle.DefaultAgent;
import agent.reporter.Reporter;
import agent.reporter.impl.FileReporter;
import agent.reporter.impl.HttpReporter;
import agent.schedular.IndividualCollectorScheduler;
import agent.schedular.Scheduler;
import agent.transport.TransportInitializer;
import agent.telemetry.dispatcher.TelemetryDispatcher;
import configuration.AgentConfig;

import java.nio.file.Paths;
import java.util.List;

public class AgentLauncher {

    public Agent launch(AgentConfig config){


        ConnectionStateManager connectionStateManager = new ConnectionStateManager();

        HeartbeatService heartbeatService = new HeartbeatInitializer(config,connectionStateManager).initialize();

        TelemetryDispatcher telemetryDispatcher = new TransportInitializer(config).initialize(connectionStateManager);

        List<Reporter> reporters = List.of(
//                new ConsoleReporter(),
                new FileReporter(Paths.get("logs")),
                new HttpReporter(telemetryDispatcher)
        );


        CollectorEngine collectorEngine = new CollectorInitializer().initialize(reporters);

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
