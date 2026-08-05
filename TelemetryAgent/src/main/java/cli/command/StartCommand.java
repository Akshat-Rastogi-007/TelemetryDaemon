package cli.command;

import agent.launcher.AgentLauncher;
import agent.lifecycle.Agent;
import configuration.AgentConfig;
import configuration.bootstrap.ConfigurationBootstrap;
import configuration.loader.ConfigurationLoader;
import configuration.source.ConfigurationSource;
import configuration.source.argunments.ArgumentsConfigurationSource;
import configuration.source.defaultprovider.DefaultConfigurationSource;
import configuration.source.properties.PropertiesConfigurationSource;
import configuration.validation.ConfigurationValidator;
import exceptions.AgentNotRegisteredException;
import identity.AgentIdentity;
import identity.bootstrap.IdentityBootstrap;
import identity.loader.IdentityLoader;
import identity.service.IdentityService;
import identity.source.IdentitySource;
import identity.source.file.FileIdentitySource;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StartCommand implements Command{

    private final String[] args;


    public StartCommand(String[] args) {
        this.args = args;
    }

    @Override
    public void execute() {


        IdentityService identityService = IdentityBootstrap.initialize();

        AgentIdentity agentIdentity = identityService.getIdentity();


        if (agentIdentity.getAgentToken() == null || agentIdentity.getAgentToken().isBlank() ){

            throw new AgentNotRegisteredException(
                    "Agent is not registered. Please run telemetry login first."
            );

        }

        AgentConfig config = ConfigurationBootstrap.initialize(args);

        System.out.println(config);

        AgentLauncher launcher = new AgentLauncher();

        Agent agent = launcher.launch(config);

        try {

            agent.awaitTermination();

        }
        catch (InterruptedException e){

            Thread.currentThread().interrupt();

            agent.stop();

        }
    }


}
