package identity.source;

import identity.AgentIdentity;

public interface IdentitySource {

    void load(AgentIdentity agentIdentity);

    void save(AgentIdentity identity);


    boolean exists();

    void delete();

}
