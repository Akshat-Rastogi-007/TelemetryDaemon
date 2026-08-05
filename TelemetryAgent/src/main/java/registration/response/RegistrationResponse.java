package registration.response;

public class RegistrationResponse {

    private String agentId;
    private String agentToken;
    private String identifier;

    public RegistrationResponse() {
    }

    public RegistrationResponse(String agentId, String agentToken,String identifier) {
        this.agentId = agentId;
        this.agentToken = agentToken;
        this.identifier = identifier;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getAgentToken() {
        return agentToken;
    }

    public void setAgentToken(String agentToken) {
        this.agentToken = agentToken;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
