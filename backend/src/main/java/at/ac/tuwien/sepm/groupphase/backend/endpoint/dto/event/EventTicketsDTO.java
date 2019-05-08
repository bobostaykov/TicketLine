package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.event;

import io.swagger.annotations.ApiModel;

import java.util.Objects;

@ApiModel(value = "EventTicketsDTO", description = "A DTO for eventTicket entries vie rest")
public class EventTicketsDTO {

    private String eventName;
    private Long ticketsSold;

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
        EventTicketsDTO that = (EventTicketsDTO) o;
        return eventName.equals(that.eventName) &&
            ticketsSold.equals(that.ticketsSold);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventName, ticketsSold);
    }

    @Override
    public String toString() {
        return "EventTicketsDTO{" +
            "eventName='" + eventName + '\'' +
            ", ticketsSold=" + ticketsSold +
            '}';
    }

    public static EventTicketsDTOBuilder builder() { return new EventTicketsDTOBuilder(); }

    public static final class EventTicketsDTOBuilder {

        private String eventName;
        private Long ticketsSold;

        private EventTicketsDTOBuilder(){}

        public EventTicketsDTOBuilder eventName(String eventName) {
            this.eventName = eventName;
            return this;
        }

        public EventTicketsDTOBuilder ticketsSold(Long ticketsSold) {
            this.ticketsSold = ticketsSold;
            return this;
        }

        public EventTicketsDTO build() {
            EventTicketsDTO eventTicketsDTO = new EventTicketsDTO();
            eventTicketsDTO.setEventName(eventName);
            eventTicketsDTO.setTicketsSold(ticketsSold);
            return eventTicketsDTO;
        }

    }
}