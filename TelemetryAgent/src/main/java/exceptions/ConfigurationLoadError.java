package exceptions;

public class ConfigurationLoadError extends RuntimeException {
    public ConfigurationLoadError(String message) {
        super(message);
    }
}
