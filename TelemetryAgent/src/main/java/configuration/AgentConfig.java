package configuration;

import java.time.Duration;


public class AgentConfig {

    private String agentId;
    private String serverUrl;
    private Duration heartbeatDuration;

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }


    public Duration getHeartbeatDuration() {
        return heartbeatDuration;
    }

    public void setHeartbeatDuration(Duration heartbeatDuration) {
        this.heartbeatDuration = heartbeatDuration;
    }



    @Override
    public String toString() {
        return "AgentConfig{" +
                "agentId='" + agentId + '\'' +
                ", serverUrl='" + serverUrl + '\'' +
                ", heartbeatDuration=" + heartbeatDuration +
                '}';
    }

}
