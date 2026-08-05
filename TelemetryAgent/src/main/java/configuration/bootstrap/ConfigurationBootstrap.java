package configuration.bootstrap;

import configuration.AgentConfig;
import configuration.loader.ConfigurationLoader;
import configuration.source.ConfigurationSource;
import configuration.source.argunments.ArgumentsConfigurationSource;
import configuration.source.defaultprovider.DefaultConfigurationSource;
import configuration.source.properties.PropertiesConfigurationSource;
import configuration.validation.ConfigurationValidator;

import java.util.Arrays;
import java.util.List;

public class ConfigurationBootstrap {


    public static AgentConfig initialize(String[] args) {

        List<ConfigurationSource> sources = Arrays.asList(
                new DefaultConfigurationSource(),
                new PropertiesConfigurationSource(),
                new ArgumentsConfigurationSource(args)
        );

        ConfigurationValidator validator =
                new ConfigurationValidator();

        ConfigurationLoader loader =
                new ConfigurationLoader(sources, validator);

        return loader.loadConfiguration();
    }
}
