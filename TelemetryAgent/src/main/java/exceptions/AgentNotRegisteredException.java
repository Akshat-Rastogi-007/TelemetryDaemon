package exceptions;

public class AgentNotRegisteredException extends RuntimeException {

    public AgentNotRegisteredException(String message) {
        super(message);
    }
}