package configuration.loader;

import configuration.AgentConfig;
import configuration.source.ConfigurationSource;
import configuration.validation.ConfigurationValidator;

import java.util.List;

public class ConfigurationLoader {

    private final List<ConfigurationSource> configurationSources;
    private final ConfigurationValidator configurationValidator;

    public ConfigurationLoader(List<ConfigurationSource> configurationSources, ConfigurationValidator configurationValidator) {
        this.configurationSources = configurationSources;
        this.configurationValidator = configurationValidator;
    }


    public AgentConfig loadConfiguration(){

        AgentConfig agentConfig = new AgentConfig();

        for (ConfigurationSource source : configurationSources )
            source.load(agentConfig);


        // validate
        configurationValidator.validate(agentConfig);

        return agentConfig;
    }


}
