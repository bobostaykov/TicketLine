package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.datatype.TicketStatus;

import javax.persistence.*;

@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "show_id")
    private Show show;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "customer_id")
    private Customer customer;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = true)
    private Integer seatNumber;

    @Column(nullable = true)
    private Integer rowNumber;

    @Column(nullable = true)
    private Integer sectorNumber;

    @Column(nullable = false)
    private TicketStatus status;

    public void setId(Long reservationNumber) {
        this.id = reservationNumber;
    }

    public Long getId() {
        return id;
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

    public void setSeatNumber(Integer seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Integer getSeatNumber() {
        return seatNumber;
    }

    public void setRowNumber(Integer rowNumber) {
        this.rowNumber = rowNumber;
    }

    public Integer getRowNumber() {
        return rowNumber;
    }

    public void setSectorNumber(Integer sectorNumber) {
        this.sectorNumber = sectorNumber;
    }

    public Integer getSectorNumber() {
        return sectorNumber;
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
        if (seatNumber != null) {
            out = out + ", seatNumber=" + seatNumber;
        }
        if (rowNumber != null) {
            out = out + ", rowNumber =" + rowNumber;
        }
        if (sectorNumber != null) {
            out = out + ", sectorNumber =" + sectorNumber;
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
        if (seatNumber != null ? !seatNumber.equals(ticket.seatNumber) : ticket.seatNumber != null) return false;
        if (rowNumber != null ? !rowNumber.equals(ticket.rowNumber) : ticket.rowNumber != null) return false;
        if (status != null ? !status.equals(ticket.status) : ticket.status != null) return false;
        return sectorNumber != null ? sectorNumber.equals(ticket.sectorNumber) : ticket.sectorNumber == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (show != null ? show.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (customer != null ? customer.hashCode() : 0);
        result = 31 * result + (seatNumber != null ? seatNumber.hashCode() : 0);
        result = 31 * result + (rowNumber != null ? rowNumber.hashCode() : 0);
        result = 31 * result + (sectorNumber != null ? sectorNumber.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }

    public static final class TicketBuilder {

        private Long id;
        private Show show;
        private Double price;
        private Customer customer;
        private Integer seatNumber;
        private Integer rowNumber;
        private Integer sectorNumber;
        private TicketStatus status;

        public TicketBuilder id(Long reservationNumber) {
            this.id = reservationNumber;
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

        public TicketBuilder seatNumber(Integer seatNumber) {
            this.seatNumber = seatNumber;
            return this;
        }

        public TicketBuilder rowNumber(Integer rowNumber) {
            this.rowNumber = rowNumber;
            return this;
        }

        public TicketBuilder sectorNumber(Integer sectorNumber) {
            this.sectorNumber = sectorNumber;
            return this;
        }

        public TicketBuilder status(TicketStatus status) {
            this.status = status;
            return this;
        }

        public Ticket build() {
            Ticket ticket = new Ticket();
            ticket.setId(id);
            ticket.setShow(show);
            ticket.setPrice(price);
            ticket.setCustomer(customer);
            ticket.setSeatNumber(seatNumber);
            ticket.setRowNumber(rowNumber);
            ticket.setSectorNumber(sectorNumber);
            ticket.setStatus(status);
            return ticket;
        }
    }

}
