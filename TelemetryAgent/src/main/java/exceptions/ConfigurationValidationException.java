package exceptions;

public class ConfigurationValidationException extends RuntimeException {
    public ConfigurationValidationException(String message) {
        super(message);
    }
}
