package configuration.source;

import configuration.AgentConfig;

public interface ConfigurationSource {

    void load(AgentConfig config);

}
