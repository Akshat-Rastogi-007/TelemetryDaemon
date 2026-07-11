package configuration.validation;

import configuration.AgentConfig;
import exceptions.ConfigurationLoadError;
import exceptions.ConfigurationValidationError;

import java.time.Duration;

public class ConfigurationValidator {


    public void validate(AgentConfig config){

        if ( config.getServerUrl() == null){

            throw new ConfigurationValidationError("Server Url Is NULL");

        }


        if ( config.getHeartbeatDuration() == null || config.getHeartbeatDuration().isNegative() || config.getHeartbeatDuration().isZero()){
            throw new ConfigurationLoadError("Heartbeat Duration is not specified");
        }


    }

}
