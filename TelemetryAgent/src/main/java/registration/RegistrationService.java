package registration;


import configuration.AgentConfig;
import exceptions.RegistrationException;
import identity.AgentIdentity;
import identity.service.IdentityService;
import registration.client.RegistrationClient;
import registration.request.RegistrationRequest;
import registration.response.RegistrationResponse;

import java.io.Console;
import java.util.Scanner;

public class RegistrationService {

    private final RegistrationClient client;
    private final IdentityService identityService;
    private final AgentConfig agentConfig;

    public RegistrationService(RegistrationClient client, IdentityService identityService, AgentConfig agentConfig) {
        this.client = client;
        this.identityService = identityService;
        this.agentConfig = agentConfig;
    }


    /*
    If you have Agent Token present then it won't be asking for the PAT TOKEN

    */

    public void authenticate(){

        Console console = System.console();

        if (console == null) {
            throw new RegistrationException(
                    "No console available. Unable to securely read the Personal Access Token."
            );
        }

        char[] patChars = console.readPassword(
                "Enter your Personal Access Token (PAT): "
        );

        String personalAccessToken = new String(patChars);

        if ( personalAccessToken.isBlank() ) {

            throw new RegistrationException("PAT token is not correct, kindly try again");

        }

        RegistrationRequest registrationClientObject = getRegistrationClientObject();


        RegistrationResponse registrationResponse = client.register(registrationClientObject, personalAccessToken, agentConfig);


        identityService.updateAgentToken(registrationResponse.getAgentToken()
                ,registrationResponse.getAgentId());

    }


    private RegistrationRequest getRegistrationClientObject(){

        AgentIdentity identity = identityService.getIdentity();

        RegistrationRequest request = new RegistrationRequest();

        request.setInstallationId(identity.getInstallationId());

        request.setVersion(agentConfig.getVersion());

        request.setJavaVersion(System.getProperty("java.version"));
        request.setOperatingSystem(System.getProperty("os.name"));
        request.setArchitecture(System.getProperty("os.arch"));

        return request;
    }

}
