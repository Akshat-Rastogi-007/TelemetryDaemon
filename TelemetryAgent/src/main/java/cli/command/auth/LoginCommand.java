package cli.command.auth;

import cli.command.Command;
import com.fasterxml.jackson.databind.ObjectMapper;
import configuration.AgentConfig;
import configuration.bootstrap.ConfigurationBootstrap;
import identity.bootstrap.IdentityBootstrap;
import identity.service.IdentityService;
import registration.RegistrationService;
import registration.request.HttpRegistrationClient;
import java.net.http.HttpClient;

public class LoginCommand implements Command {

    private final String[] args;

    public LoginCommand(String[] args) {
        this.args = args;
    }


    @Override
    public void execute() {

        ObjectMapper objectMapper = new ObjectMapper();


        AgentConfig config = ConfigurationBootstrap.initialize(args);

        IdentityService identityService = IdentityBootstrap.initialize();



        HttpRegistrationClient httpRegistrationClient = new HttpRegistrationClient(objectMapper, HttpClient.newBuilder().build());

        RegistrationService registrationService = new RegistrationService(httpRegistrationClient, identityService, config);

        registrationService.authenticate();

        loggedInOutput();

    }

    private static void loggedInOutput() {
        System.out.println();
        System.out.println("========================================");
        System.out.println("Successfully logged in!");
        System.out.println("Your Telemetry Agent has been registered.");
        System.out.println("Run 'telemetry-agent start' to start the agent.");
        System.out.println("========================================");
    }


}
