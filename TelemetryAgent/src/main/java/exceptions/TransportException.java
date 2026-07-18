package exceptions;

public class TransportException extends RuntimeException {

    public TransportException(String message, Exception e) {
        super(message,e);
    }
    public TransportException(String message) {
        super(message);
    }
}
