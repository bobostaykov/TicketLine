package at.ac.tuwien.sepm.groupphase.backend.exception;

public class TicketSoldOutException extends Exception{
    public TicketSoldOutException(String message, Throwable cause) {
        super(message, cause);
    }
    public TicketSoldOutException(String message) {
        super(message);
    }
}
