package ioc.exceptions;

public class PostConstructInstantiationException extends RuntimeException {
    public PostConstructInstantiationException(String message) {
        super(message);
    }

    public PostConstructInstantiationException(String message, Throwable cause) {
        super(message, cause);
    }
}
