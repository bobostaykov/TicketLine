package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.datatype.PriceCategory;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_seat_id")
    @SequenceGenerator(name = "seq_seat_id", sequenceName = "seq_seat_id")
    private Long id;

    @Column(nullable = false)
    private Integer seatNumber;

    @Column(nullable = false)
    private Integer seatRow;

    @Column(nullable = false)
    private PriceCategory priceCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hall_id", nullable = false)
    private Hall hall;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "seat")
    private List<Ticket> tickets;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(Integer seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Integer getSeatRow() {
        return seatRow;
    }

    public void setSeatRow(Integer seatRow) {
        this.seatRow = seatRow;
    }

    public PriceCategory getPriceCategory() {
        return priceCategory;
    }

    public void setPriceCategory(PriceCategory priceCategory) {
        this.priceCategory = priceCategory;
    }

    public static SeatBuilder builder() {
        return new SeatBuilder();
    }

    public Hall getHall() {
        return hall;
    }

    public void setHall(Hall hall) {
        this.hall = hall;
    }

    public List<Ticket> getTickets(){
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    @Override
    public String toString() {
        return "Seat{" +
            "id=" + id +
            ", seatNumber=" + seatNumber +
            ", seatRow=" + seatRow +
            ", priceCategory='" + priceCategory + "\'" +
            ", hall=" + hall +
            ", tickets=" + tickets.toString() +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seat seat = (Seat) o;
        return Objects.equals(id, seat.getId()) &&
            Objects.equals(seatNumber, seat.getSeatNumber()) &&
            Objects.equals(seatRow, seat.getSeatRow()) &&
            Objects.equals(priceCategory, seat.getPriceCategory()) &&
            Objects.equals(hall, seat.getHall()) &&
            Objects.equals(tickets, seat.getTickets());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, seatNumber, seatRow, priceCategory, hall, tickets);
    }

    public static final class SeatBuilder {
        private Long id;
        private Integer seatNumber;
        private Integer seatRow;
        private PriceCategory priceCategory;
        private Hall hall;
        private List<Ticket> ticket;

        private SeatBuilder() {
        }

        public SeatBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public SeatBuilder seatNumber(Integer seatNumber) {
            this.seatNumber = seatNumber;
            return this;
        }

        public SeatBuilder seatRow(Integer seatRow) {
            this.seatRow = seatRow;
            return this;
        }

        public SeatBuilder priceCategory(PriceCategory priceCategory) {
            this.priceCategory = priceCategory;
            return this;
        }

        public SeatBuilder hall(Hall hall) {
            this.hall = hall;
            return this;
        }

        public SeatBuilder ticket(List<Ticket> ticket){
            this.ticket = ticket;
            return this;
        }

        public Seat build() {
            Seat seat = new Seat();
            seat.setId(id);
            seat.setSeatNumber(seatNumber);
            seat.setSeatRow(seatRow);
            seat.setPriceCategory(priceCategory);
            seat.setHall(hall);
            seat.setTickets(ticket);
            return seat;
        }
    }
}
