package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.datatype.TicketStatus;

import javax.persistence.*;

@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(unique = true)
    private String reservationNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "show_id")
    private Show show;

    @Column(nullable = false)
    private Double price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "seat_id")
    private Seat seat;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sector_id")
    private Sector sector;

    @Column(nullable = false)
    private TicketStatus status;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setReservationNo(String reservationNo) {
        this.reservationNo = reservationNo;
    }

    public String getReservationNo() {
        return reservationNo;
    }

    public void setShow(Show show) {
        this.show = show;
    }

    public Show getShow() {
        return show;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPrice() {
        return price;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }

    public Sector getSector() {
        return sector;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public static TicketBuilder builder() {
        return new TicketBuilder();
    }

    @Override
    public String toString() {
        String out = "Ticket{" +
            "id=" + id +
            ", show='" + show.toString() + '\'' +
            ", price=" + price +
            ", customer=" + customer.toString() +
            ", status=" + status;
        if (seat != null && seat.getSeatNumber() != null) {
            out = out + ", seatNumber=" + seat.getSeatNumber();
        }
        if (seat != null && seat.getSeatRow() != null) {
            out = out + ", rowNumber =" + seat.getSeatRow();
        }
        if (sector != null) {
            out = out + ", sectorNumber =" + sector.getSectorNumber();
        }
        if (reservationNo != null) {
            out = out + ", rservationNumber = " + reservationNo;
        }
        out = out + '}';
        return out;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ticket ticket = (Ticket) o;

        if (id != null ? !id.equals(ticket.id) : ticket.id != null) return false;
        if (show != null ? !show.equals(ticket.show) : ticket.show != null) return false;
        if (price != null ? !price.equals(ticket.price) : ticket.price != null) return false;
        if (customer != null ? !customer.equals(ticket.customer) : ticket.customer != null) return false;
        if (seat != null ? !seat.equals(ticket.seat) : ticket.seat != null) return false;
        if (status != null ? !status.equals(ticket.status) : ticket.status != null) return false;
        if (reservationNo != null ? !reservationNo.equals(ticket.reservationNo) : ticket.reservationNo != null) return false;
        return sector != null ? sector.equals(ticket.sector) : ticket.sector == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (show != null ? show.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (customer != null ? customer.hashCode() : 0);
        result = 31 * result + (seat != null ? seat.hashCode() : 0);
        result = 31 * result + (sector != null ? sector.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (reservationNo != null ? reservationNo.hashCode() : 0);
        return result;
    }

    public static final class TicketBuilder {

        private Long id;
        private String reservationNo;
        private Show show;
        private Double price;
        private Customer customer;
        private Seat seat;
        private Sector sector;
        private TicketStatus status;

        public TicketBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public TicketBuilder reservationNo(String reservationNo) {
            this.reservationNo = reservationNo;
            return this;
        }

        public TicketBuilder show(Show show) {
            this.show = show;
            return this;
        }

        public TicketBuilder price(Double price) {
            this.price = price;
            return this;
        }
        public TicketBuilder customer(Customer customer) {
            this.customer = customer;
            return this;
        }

        public TicketBuilder seat(Seat seat) {
            this.seat = seat;
            return this;
        }

        public TicketBuilder sector(Sector sector) {
            this.sector = sector;
            return this;
        }

        public TicketBuilder status(TicketStatus status) {
            this.status = status;
            return this;
        }

        public Ticket build() {
            Ticket ticket = new Ticket();
            ticket.setId(id);
            ticket.setReservationNo(reservationNo);
            ticket.setShow(show);
            ticket.setPrice(price);
            ticket.setCustomer(customer);
            ticket.setSeat(seat);
            ticket.setSector(sector);
            ticket.setStatus(status);
            return ticket;
        }
    }

}
