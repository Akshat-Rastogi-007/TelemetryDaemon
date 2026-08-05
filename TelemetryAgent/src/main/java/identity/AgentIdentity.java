package identity;

public class AgentIdentity {

    private String agentId;
    private String agentToken;
    private String installationId;

    public AgentIdentity() {
    }

    public AgentIdentity(String agentId, String installationId, String agentToken) {
        this.agentId = agentId;
        this.installationId = installationId;
        this.agentToken  = agentToken;
    }


    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getInstallationId() {
        return installationId;
    }

    public String getAgentToken() {
        return agentToken;
    }


    public void setAgentToken(String agentToken){

        this.agentToken = agentToken;

    }

    public void setInstallationId(String installationId){

        this.installationId = installationId;
    }

    @Override
    public String toString() {
        return "AgentIdentity{" +
                "agentId='" + agentId + '\'' +
                ", agentToken='" + agentToken + '\'' +
                ", installationId='" + installationId + '\'' +
                '}';
    }
}
