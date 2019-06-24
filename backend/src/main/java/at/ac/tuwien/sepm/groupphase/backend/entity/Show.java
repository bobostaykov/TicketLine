package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Entity
public class Show {

    @Id
    @SequenceGenerator(name = "show_seq", initialValue = 1, allocationSize = 50)
    @GeneratedValue(generator = "show_seq")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "event_id")
    private Event event;

    @Column(nullable = false, name = "time")
    private LocalTime time;

    @Column(nullable = false, name = "date")
    private LocalDate date;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "hall_id")
    private Hall hall;

    @Column(name = "description")
    @Size(max = 256)
    private String description;

    @Column(nullable = false, name = "tickets_sold")
    @PositiveOrZero
    private Long ticketsSold;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pricePattern_id")
    private PricePattern pricePattern;

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

    public LocalDate getDate() {return date;}

    public void setDate(LocalDate date) {this.date = date;}

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {this.time = time;}

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

    public Long getTicketsSold() {
        return ticketsSold;
    }

    public void setTicketsSold(Long ticketsSold) {
        this.ticketsSold = ticketsSold;
    }

    public PricePattern getPricePattern() {
        return pricePattern;
    }

    public void setPricePattern(PricePattern pricePattern) {
        this.pricePattern = pricePattern;
    }

    public static ShowBuilder builder() {
        return new ShowBuilder();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Show show = (Show) o;
        return Objects.equals(id, show.id) &&
            Objects.equals(event, show.event) &&
            Objects.equals(time, show.time) &&
            Objects.equals(date, show.date) &&
            Objects.equals(hall, show.hall) &&
            Objects.equals(description, show.description) &&
            Objects.equals(ticketsSold, show.ticketsSold);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, event, time, date, hall, description, ticketsSold);
    }

    @Override
    public String toString() {
        return "Show{" +
            "id=" + id +
            ", event=" + event +
            ", time=" + time +
            ", date=" + date +
            ", hall=" + hall +
            ", description='" + description + '\'' +
            ", ticketsSold=" + ticketsSold +
            ", pricePattern=" + pricePattern +
            '}';
    }

    public static final class ShowBuilder {
        private Long id;
        private Event event;
        private LocalDate date;
        private LocalTime time;
        private Hall hall;
        private String description;
        private Long ticketsSold;
        private PricePattern pricePattern;

        private ShowBuilder() {}

        public ShowBuilder pricePattern(PricePattern pricePattern){
            this.pricePattern = pricePattern;
            return this;
        }

        public ShowBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ShowBuilder event(Event event) {
            this.event = event;
            return this;
        }


        public ShowBuilder date(LocalDate date) {
            this.date = date;
            return this;
        }

        public ShowBuilder time(LocalTime time) {
            this.time = time;
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

        public ShowBuilder ticketsSold(Long ticketsSold) {
            this.ticketsSold = ticketsSold;
            return this;
        }

        public Show build() {
            Show show = new Show();
            show.setId(id);
            show.setEvent(event);
            show.setDate(date);
            show.setTime(time);
            show.setHall(hall);
            show.setDescription(description);
            show.setTicketsSold(ticketsSold);
            show.setPricePattern(pricePattern);
            return show;
        }
    }
}
