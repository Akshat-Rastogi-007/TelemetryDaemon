package agent.launcher;

import agent.lifecycle.Agent;
import agent.lifecycle.DefaultAgent;
import agent.schedular.DefaultSchedular;
import agent.schedular.Scheduler;
import configuration.AgentConfig;

public class AgentLauncher {

    public Agent launch(AgentConfig config){

        System.out.println("*****LAUNCHING AGENT*****");
        Scheduler scheduler = new DefaultSchedular();
        Agent agent = new DefaultAgent(config,scheduler);

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
