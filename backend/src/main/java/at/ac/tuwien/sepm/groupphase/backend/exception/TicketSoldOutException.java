package at.ac.tuwien.sepm.groupphase.backend.exception;

public class TicketSoldOutException extends RuntimeException{
    public TicketSoldOutException(String message) {
        super(message);
    }
}
