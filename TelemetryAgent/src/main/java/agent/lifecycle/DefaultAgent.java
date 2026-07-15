package agent.lifecycle;

import agent.collector.CollectorEngine;
import agent.enums.AgentState;
import agent.schedular.Scheduler;
import configuration.AgentConfig;
import exceptions.AgentLifecycleException;

import java.util.concurrent.CountDownLatch;

public class DefaultAgent implements Agent{

    private AgentState state =  AgentState.NEW;
    private final AgentConfig agentConfig;
    private final CountDownLatch terminationLatch = new CountDownLatch(1);
    private final CollectorEngine collectorEngine;


    public DefaultAgent(AgentConfig agentConfig, CollectorEngine collectorEngine) {
        this.agentConfig = agentConfig;
        this.collectorEngine = collectorEngine;
    }

    @Override
    public void start() {

        if (state == AgentState.RUNNING){
            throw new AgentLifecycleException("Agent is already running");
        }

        System.out.println("*****Starting AGENT*****");
        state = AgentState.STARTING;

        collectorEngine.start();

        collectorEngine.scheduleCollector(agentConfig);


        state = AgentState.RUNNING;
    }

    @Override
    public void stop() {


        if (state == AgentState.STOPPED){
            return;
        }


        state = AgentState.STOPPING;

        collectorEngine.stop();

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
