package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.datatype.PriceCategory;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Seat {

    @Id
    @SequenceGenerator(name = "seat_seq", initialValue = 1, allocationSize = 50)
    @GeneratedValue(generator = "seat_seq")
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

    @Override
    public String toString() {
        return "Seat{" +
            "id=" + id +
            ", seatNumber=" + seatNumber +
            ", seatRow=" + seatRow +
            ", priceCategory='" + priceCategory + "\'" +
            ", hall=" + hall +
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
            Objects.equals(hall, seat.getHall());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, seatNumber, seatRow, priceCategory, hall);
    }

    public static final class SeatBuilder {
        private Long id;
        private Integer seatNumber;
        private Integer seatRow;
        private PriceCategory priceCategory;
        private Hall hall;

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

        public Seat build() {
            Seat seat = new Seat();
            seat.setId(id);
            seat.setSeatNumber(seatNumber);
            seat.setSeatRow(seatRow);
            seat.setPriceCategory(priceCategory);
            seat.setHall(hall);
            return seat;
        }
    }
}
