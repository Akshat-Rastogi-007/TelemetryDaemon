package configuration.validation;

import configuration.AgentConfig;
import exceptions.ConfigurationLoadException;
import exceptions.ConfigurationValidationException;

import java.net.URI;

public class ConfigurationValidator {


    public void validate(AgentConfig config){

        String serverUrl = config.getServerUrl();

        if (serverUrl == null || serverUrl.isBlank()) {
            throw new ConfigurationValidationException("Server URL must be configured.");
        }

        try {
            URI.create(serverUrl);
        } catch (IllegalArgumentException e) {
            throw new ConfigurationValidationException(
                    "Invalid server URL: " + serverUrl, e);
        }


        if ( config.getHeartbeatDuration() == null || config.getHeartbeatDuration().isNegative() || config.getHeartbeatDuration().isZero()){
            throw new ConfigurationLoadException("Heartbeat Duration is not specified");
        }


    }

}
