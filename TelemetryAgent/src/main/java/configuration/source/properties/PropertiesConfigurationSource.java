package configuration.source.properties;

import configuration.AgentConfig;
import configuration.source.ConfigurationSource;
import exceptions.ConfigurationLoadError;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Properties;

public class PropertiesConfigurationSource implements ConfigurationSource {


    @Override
    public void load(AgentConfig config) {

        Path path = Path.of("config/config.properties");

        Properties properties = new Properties();

        try (BufferedReader reader =  Files.newBufferedReader(path)){

            properties.load(reader);

            config.setServerUrl(properties.getProperty("server.url"));
            config.setAgentId(properties.getProperty("agent.id"));
            config.setHeartbeatDuration(
                    Duration.ofSeconds(Long.parseLong(properties.getProperty("heartbeat.interval")))
            );

        } catch (IOException e) {
            throw new ConfigurationLoadError( "Error loading the configuration due to : "+ e.getMessage());
        }

    }
}
