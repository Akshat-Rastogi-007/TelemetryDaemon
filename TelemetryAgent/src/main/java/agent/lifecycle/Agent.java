package agent.lifecycle;

import agent.enums.AgentState;

public interface Agent {

    void start();

    void stop();

    void awaitTermination() throws InterruptedException;

    AgentState getState();

}
