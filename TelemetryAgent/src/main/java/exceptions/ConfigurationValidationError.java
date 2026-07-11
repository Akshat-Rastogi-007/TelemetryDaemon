package exceptions;

public class ConfigurationValidationError extends RuntimeException {
    public ConfigurationValidationError(String message) {
        super(message);
    }
}
