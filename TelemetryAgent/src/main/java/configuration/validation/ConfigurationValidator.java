package configuration.validation;

import configuration.AgentConfig;
import exceptions.ConfigurationLoadException;
import exceptions.ConfigurationValidationException;

public class ConfigurationValidator {


    public void validate(AgentConfig config){

        if ( config.getServerUrl() == null){

            throw new ConfigurationValidationException("Server Url Is NULL");

        }


        if ( config.getHeartbeatDuration() == null || config.getHeartbeatDuration().isNegative() || config.getHeartbeatDuration().isZero()){
            throw new ConfigurationLoadException("Heartbeat Duration is not specified");
        }


    }

}
