package registration.client;

import configuration.AgentConfig;
import registration.request.RegistrationRequest;
import registration.response.RegistrationResponse;

public interface RegistrationClient {

    RegistrationResponse register(RegistrationRequest request, String patToken, AgentConfig config);
}
