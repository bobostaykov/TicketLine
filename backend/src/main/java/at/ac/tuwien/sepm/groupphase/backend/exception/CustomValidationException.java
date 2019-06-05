package at.ac.tuwien.sepm.groupphase.backend.exception;

public class CustomValidationException extends Exception {
    public CustomValidationException(String message, Throwable cause) {
        super(message, cause);
    }
    public CustomValidationException(String message) {
        super(message);
    }
}
