package at.ac.tuwien.sepm.groupphase.backend.exception;

public class PersistenceException extends Exception{
    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
    public PersistenceException(String message) {
        super(message);
    }
}

