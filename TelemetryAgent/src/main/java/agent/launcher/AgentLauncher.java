package agent.launcher;

import agent.lifecycle.Agent;
import agent.lifecycle.DefaultAgent;
import configuration.AgentConfig;

public class AgentLauncher {

    public Agent launch(AgentConfig config){

        Agent agent = new DefaultAgent(config);

        registerShutDownHook(agent);

        agent.start();

        return agent;

    }

    private static void registerShutDownHook(Agent agent) {
        Runtime.getRuntime().addShutdownHook(new Thread(agent::stop));
    }


}
