package exceptions;

public class ConfigurationLoadException extends RuntimeException {
    public ConfigurationLoadException(String message) {
        super(message);
    }
}
