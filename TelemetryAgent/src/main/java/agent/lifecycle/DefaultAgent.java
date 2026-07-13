package agent.lifecycle;

import agent.enums.AgentState;
import configuration.AgentConfig;
import exceptions.AgentLifecycleException;

import java.util.concurrent.CountDownLatch;

public class DefaultAgent implements Agent{

    private AgentState state =  AgentState.NEW;
    private final AgentConfig agentConfig;
    private final CountDownLatch terminationLatch = new CountDownLatch(1);


    public DefaultAgent(AgentConfig agentConfig) {
        this.agentConfig = agentConfig;
    }

    @Override
    public void start() {

        if (state == AgentState.RUNNING){
            throw new AgentLifecycleException("Agent is already running");
        }

        state = AgentState.STARTING;

        // some process

        state = AgentState.RUNNING;
    }

    @Override
    public void stop() {


        if (state == AgentState.STOPPED){
            return;
        }

        state = AgentState.STOPPING;

        // some process

        state = AgentState.STOPPED;

        terminationLatch.countDown();
    }

    @Override
    public void awaitTermination() throws InterruptedException {

        terminationLatch.await();

    }

    @Override
    public AgentState getState() {
        return state;
    }
}
