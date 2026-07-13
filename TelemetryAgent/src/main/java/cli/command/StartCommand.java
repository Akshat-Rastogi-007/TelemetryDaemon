package cli.command;

import agent.launcher.AgentLauncher;
import agent.lifecycle.Agent;
import configuration.AgentConfig;
import configuration.loader.ConfigurationLoader;
import configuration.source.ConfigurationSource;
import configuration.source.argunments.ArgumentsConfigurationSource;
import configuration.source.defaultprovider.DefaultConfigurationSource;
import configuration.source.properties.PropertiesConfigurationSource;
import configuration.validation.ConfigurationValidator;

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


        List<ConfigurationSource> sources = Arrays.asList(
                new DefaultConfigurationSource(),
                new PropertiesConfigurationSource(),
                new ArgumentsConfigurationSource(args)
        );

        ConfigurationValidator validator = new ConfigurationValidator();
        ConfigurationLoader loader = new ConfigurationLoader(sources, validator);

        AgentConfig config = loader.loadConfiguration();

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
