package at.ac.tuwien.sepm.groupphase.backend.entity;

import java.util.Objects;

public class EventTickets {

    private String eventName;
    private Long ticketsSold;

    public EventTickets(){}

    public EventTickets(String eventName, Long ticketsSold) {
        this.eventName = eventName;
        this.ticketsSold = ticketsSold;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Long getTicketsSold() {
        return ticketsSold;
    }

    public void setTicketsSold(Long ticketsSold) {
        this.ticketsSold = ticketsSold;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventTickets that = (EventTickets) o;
        return eventName.equals(that.eventName) &&
            ticketsSold.equals(that.ticketsSold);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventName, ticketsSold);
    }

    @Override
    public String toString() {
        return "EventTickets{" +
            "eventName='" + eventName + '\'' +
            ", ticketsSold=" + ticketsSold +
            '}';
    }

    public static EventTicketsBuilder builder() { return new EventTicketsBuilder(); }

    public static final class EventTicketsBuilder {

        private String eventName;
        private Long ticketsSold;

        private EventTicketsBuilder(){}

        public EventTicketsBuilder eventName(String eventName) {
            this.eventName = eventName;
            return this;
        }

        public EventTicketsBuilder ticketsSold(Long ticketsSold) {
            this.ticketsSold = ticketsSold;
            return this;
        }

        public EventTickets build() {
            EventTickets eventTickets = new EventTickets();
            eventTickets.setEventName(eventName);
            eventTickets.setTicketsSold(ticketsSold);
            return eventTickets;
        }

    }

}
