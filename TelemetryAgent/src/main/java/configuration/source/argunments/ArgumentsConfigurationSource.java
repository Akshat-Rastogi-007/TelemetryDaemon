package configuration.source.argunments;

import configuration.AgentConfig;
import configuration.source.ConfigurationSource;

import java.time.Duration;

public class ArgumentsConfigurationSource implements ConfigurationSource {

    private final String[] args;

    public ArgumentsConfigurationSource(String[] args) {
        this.args = args;
    }

    @Override
    public void load(AgentConfig config) {

        for ( String arg : args){

            if ( arg.startsWith("--server=")) {
                String serverUrl = extractValue(arg);
                config.setServerUrl(serverUrl);
            }
            else if ( arg.startsWith("--heartbeat=")){
                long heartbeat = Long.parseLong(extractValue(arg));
                config.setHeartbeatDuration(Duration.ofSeconds(heartbeat));
            }

        }

    }

    private static String extractValue(String arg) {
        return arg.split("=", 2)[1];
    }
}
