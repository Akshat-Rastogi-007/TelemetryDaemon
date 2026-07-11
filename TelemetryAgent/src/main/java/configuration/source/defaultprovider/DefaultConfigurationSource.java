package configuration.source.defaultprovider;

import configuration.AgentConfig;
import configuration.source.ConfigurationSource;

import java.time.Duration;

public class DefaultConfigurationSource implements ConfigurationSource {


    @Override
    public void load(AgentConfig config) {

        config.setHeartbeatDuration(Duration.ofSeconds(30));

    }
}
