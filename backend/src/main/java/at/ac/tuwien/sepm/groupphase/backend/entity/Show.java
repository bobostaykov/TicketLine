package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Show {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_show_id")
    @SequenceGenerator(name = "seq_show_id", sequenceName = "seq_show_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "event_id")
    private Event event;

    @Column(nullable = false, name = "dateTime")
    private LocalDateTime dateTime;

    @Column(nullable = false, name = "duration")
    @Positive
    private Integer durationInMinutes;

    @Column(name = "ticketsSold")
    @PositiveOrZero
    private Integer ticketsSold;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "hall_id")
    private Hall hall;

    @Column(name = "description")
    @Size(max = 256)
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Hall getHall() {
        return hall;
    }

    public void setHall(Hall hall) {
        this.hall = hall;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(Integer durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public Integer getTicketsSold() {
        return ticketsSold;
    }

    public void setTicketsSold(Integer ticketsSold) {
        this.ticketsSold = ticketsSold;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Show show = (Show) o;
        return id.equals(show.id) &&
            event.equals(show.event) &&
            dateTime.equals(show.dateTime) &&
            durationInMinutes.equals(show.durationInMinutes) &&
            ticketsSold.equals(show.ticketsSold) &&
            hall.equals(show.hall) &&
            Objects.equals(description, show.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, event, dateTime, durationInMinutes, ticketsSold, hall, description);
    }

    @Override
    public String toString() {
        return "Show{" +
            "id=" + id +
            ", event=" + event +
            ", dateTime=" + dateTime +
            ", durationInMinutes=" + durationInMinutes +
            ", ticketsSold=" + ticketsSold +
            ", hall=" + hall +
            ", description='" + description + '\'' +
            '}';
    }

    public static final class ShowBuilder {
        private Long id;
        private Event event;
        private LocalDateTime dateTime;
        private Integer durationInMinutes;
        private Integer ticketsSold;
        private Hall hall;
        private String description;

        private ShowBuilder() {}

        public ShowBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ShowBuilder event(Event event) {
            this.event = event;
            return this;
        }

        public ShowBuilder durationInMinutes(Integer durationInMinutes) {
            this.durationInMinutes = durationInMinutes;
            return this;
        }

        public ShowBuilder ticketsSold(Integer ticketsSold) {
            this.ticketsSold = ticketsSold;
            return this;
        }

        public ShowBuilder dateTime(LocalDateTime dateTime) {
            this.dateTime = dateTime;
            return this;
        }

        public ShowBuilder hall(Hall hall) {
            this.hall = hall;
            return this;
        }

        public ShowBuilder description(String description) {
            this.description = description;
            return this;
        }

        public Show build() {
            Show show = new Show();
            show.setId(id);
            show.setEvent(event);
            show.setDurationInMinutes(durationInMinutes);
            show.setTicketsSold(ticketsSold);
            show.setDateTime(dateTime);
            show.setHall(hall);
            show.setDescription(description);
            return show;
        }
    }
}
