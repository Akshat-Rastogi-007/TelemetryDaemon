package exceptions;

public class ConfigurationValidationException extends RuntimeException {
    public ConfigurationValidationException(String message, Exception e) {
        super(message,e);
    }

    public ConfigurationValidationException(String message) {
        super(message);
    }
}
