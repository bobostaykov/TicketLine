package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.datatype.PriceCategory;

import javax.persistence.*;

@Entity
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_seat_id")
    @SequenceGenerator(name = "seq_seat_id", sequenceName = "seq_seat_id")
    private Integer id;

    @Column(nullable = false)
    private Integer seatNumber;

    @Column(nullable = false)
    private Integer seatRow;

    @Column(nullable = false)
    private PriceCategory priceCategory;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public static SeatBuilder builder(){
        return new SeatBuilder();
    }

    @Override
    public String toString() {
        return "Seat{" +
            "id=" + id +
            ", seatNumber=" + seatNumber +
            ", seatRow=" + seatRow +
            ", priceCategory='" + priceCategory + "\'" +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return  true;
        if(o == null || getClass() != o.getClass()) return false;
        Seat seat = (Seat) o;
        if(id != null ? ! id.equals(seat.id) : seat.id != null) return false;
        if(seatNumber != null ? ! seatNumber.equals(seat.getSeatNumber()) : seat.getSeatNumber() != null) return false;
        if(seatRow != null ? ! seatRow.equals(seat.getSeatRow()) : seat.getSeatRow() != null) return false;
        return priceCategory != null ? priceCategory.equals(seat.getPriceCategory()) : seat.getPriceCategory() == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (seatNumber != null ? seatNumber.hashCode() : 0);
        result = 31 * result + (seatRow != null ? seatRow.hashCode() : 0);
        result = 31 * result + (priceCategory != null ? priceCategory.hashCode() : 0);
        return result;
    }

    public static final class SeatBuilder{
        private Integer id;
        private Integer seatNumber;
        private Integer seatRow;
        private PriceCategory priceCategory;
        private Long hallId;

        private SeatBuilder(){}

        public SeatBuilder id(Integer id){
            this.id = id;
            return this;
        }

        public SeatBuilder seatNumber(Integer seatNumber){
            this.seatNumber = seatNumber;
            return this;
        }

        public SeatBuilder seatRow(Integer seatRow){
            this.seatRow = seatRow;
            return this;
        }

        public SeatBuilder priceCategory(PriceCategory priceCategory){
            this.priceCategory = priceCategory;
            return this;
        }

        public SeatBuilder hallId(Long hallId){
            this.hallId = hallId;
            return this;
        }

        public Seat build(){
            Seat seat = new Seat();
            seat.setId(id);
            seat.setSeatNumber(seatNumber);
            seat.setSeatRow(seatRow);
            seat.setPriceCategory(priceCategory);
            return seat;
        }
    }
}
