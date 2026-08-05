package identity.loader;


import exceptions.IdentityException;
import identity.AgentIdentity;
import identity.source.IdentitySource;

import java.util.List;

public class IdentityLoader {


    private final IdentitySource identitySource;

    public IdentityLoader(IdentitySource identitySource) {
        this.identitySource = identitySource;
    }


    public AgentIdentity load() {

        AgentIdentity agentIdentity = new AgentIdentity();

        identitySource.load(agentIdentity);

        if (agentIdentity.getInstallationId() == null || agentIdentity.getInstallationId().isBlank()) {
            throw new IdentityException("Identity is corrupted.");
        }

        return agentIdentity;
    }


}
