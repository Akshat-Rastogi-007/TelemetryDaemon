package identity.service;

import identity.AgentIdentity;
import identity.loader.IdentityLoader;
import identity.source.IdentitySource;

import java.util.UUID;

public class IdentityService {


    private final IdentityLoader identityLoader;
    private final IdentitySource identitySource;


    public IdentityService(IdentityLoader identityLoader, IdentitySource identitySource) {
        this.identityLoader = identityLoader;
        this.identitySource = identitySource;
    }


    public AgentIdentity initialize(){


        if (identitySource.exists()){

            return identityLoader.load();

        }

        return createIdentity();
    }

    private AgentIdentity createIdentity() {
        AgentIdentity identity = new AgentIdentity();
        identity.setInstallationId(UUID.randomUUID().toString());
        identitySource.save(identity);
        return identity;
    }


    public AgentIdentity getIdentity(){

        return identityLoader.load();

    }

    public void updateAgentToken(String agentToken, String agentId) {

        AgentIdentity identity = getIdentity();


        identity.setAgentId(agentId);
        identity.setAgentToken(agentToken);

        identitySource.save(identity);

    }

    public void update(AgentIdentity identity){

        identitySource.save(identity);

    }
    
    public void logout() {

        AgentIdentity identity = getIdentity();

        identity.setAgentToken(null);

        identitySource.save(identity);

    }


    public void delete(){

        identitySource.delete();;


    }

}
