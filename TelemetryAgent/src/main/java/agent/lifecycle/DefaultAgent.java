package agent.lifecycle;

import agent.enums.AgentState;
import agent.schedular.Scheduler;
import configuration.AgentConfig;
import exceptions.AgentLifecycleException;

import java.util.concurrent.CountDownLatch;

public class DefaultAgent implements Agent{

    private AgentState state =  AgentState.NEW;
    private final AgentConfig agentConfig;
    private final CountDownLatch terminationLatch = new CountDownLatch(1);
    private final Scheduler schedular;


    public DefaultAgent(AgentConfig agentConfig, Scheduler schedular) {
        this.agentConfig = agentConfig;
        this.schedular = schedular;
    }

    @Override
    public void start() {

        if (state == AgentState.RUNNING){
            throw new AgentLifecycleException("Agent is already running");
        }

        System.out.println("*****Starting AGENT*****");
        state = AgentState.STARTING;

        // some process
        schedular.start();

        System.out.println("*****SCHEDULING TASK*****");

        schedular.schedule(() -> System.out.println("Hi"), agentConfig.getHeartbeatDuration());

        state = AgentState.RUNNING;
    }

    @Override
    public void stop() {


        if (state == AgentState.STOPPED){
            return;
        }


        state = AgentState.STOPPING;

        // some process

        schedular.stop();
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
