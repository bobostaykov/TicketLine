package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ticket;

import at.ac.tuwien.sepm.groupphase.backend.datatype.TicketStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(value = "TicketPostDTO", description = "A DTO for ticket entries via rest")
public class TicketPostDTO {

    @ApiModelProperty(name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(name = "Reservation number for ticket reservations")
    private String reservationNo;

    @ApiModelProperty(name = "The show this tickets was issued for")
    private Long show;

    @ApiModelProperty(name = "Price of the ticket")
    private Double price;

    @ApiModelProperty(name = "The customer this ticket was issued for")
    private Long customer;

    @ApiModelProperty(name = "The seat number of this ticket")
    private Long seat;

    @ApiModelProperty(name = "The sector number of this ticket")
    private Long sector;

    @ApiModelProperty(name = "Status of the ticket (RESERVED, SOLD)")
    private TicketStatus status;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getReservationNo() {
        return reservationNo;
    }

    public void setReservationNo(String reservationNo) {
        this.reservationNo = reservationNo;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPrice() {
        return price;
    }

    public void setShow(Long show) {
        this.show = show;
    }

    public Long getShow() {
        return show;
    }

    public void setCustomer(Long customer) {
        this.customer = customer;
    }

    public Long getCustomer() {
        return customer;
    }

    public void setSector(Long sector) {
        this.sector = sector;
    }

    public Long getSeat() {
        return seat;
    }

    public void setSeat(Long seat) {
        this.seat = seat;
    }

    public Long getSector() {
        return sector;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public static TicketPostDTOBuilder builder() {
        return new TicketPostDTOBuilder();
    }

    @Override
    public String toString() {
        String out = "TicketPostDTO{" +
            "id=" + id +
            ", show='" + show +
            ", price=" + price +
            ", customer=" + customer+
            ", status=" + status;
        if (seat != null) {
            out = out + ", seatNumber=" + seat;
        }
        if (sector != null) {
            out = out + ", sectorNumber =" + sector;
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

        TicketPostDTO ticketPostDTO = (TicketPostDTO) o;

        if (id != null ? !id.equals(ticketPostDTO.id) : ticketPostDTO.id != null) return false;
        if (show != null ? !show.equals(ticketPostDTO.show) : ticketPostDTO.show != null) return false;
        if (price != null ? !price.equals(ticketPostDTO.price) : ticketPostDTO.price != null) return false;
        if (customer != null ? !customer.equals(ticketPostDTO.customer) : ticketPostDTO.customer != null) return false;
        if (seat != null ? !seat.equals(ticketPostDTO.seat) : ticketPostDTO.seat != null) return false;
        if (status != null ? !status.equals(ticketPostDTO.status) : ticketPostDTO.status != null) return false;
        if (reservationNo != null ? !status.equals(ticketPostDTO.reservationNo) : ticketPostDTO.reservationNo != null) return false;
        return sector != null ? sector.equals(ticketPostDTO.sector) : ticketPostDTO.sector == null;
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

    public static final class TicketPostDTOBuilder {

        private Long id;
        private String reservationNo;
        private Long show;
        private Double price;
        private Long customer;
        private Long seat;
        private Long sector;
        private TicketStatus status;

        public TicketPostDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public TicketPostDTOBuilder reservationNo(String reservationNo) {
            this.reservationNo = reservationNo;
            return this;
        }

        public TicketPostDTOBuilder show(Long show) {
            this.show = show;
            return this;
        }

        public TicketPostDTOBuilder price(Double price) {
            this.price = price;
            return this;
        }
        public TicketPostDTOBuilder customer(Long customer) {
            this.customer = customer;
            return this;
        }

        public TicketPostDTOBuilder seat(Long seat) {
            this.seat = seat;
            return this;
        }

        public TicketPostDTOBuilder sector(Long sector) {
            this.sector = sector;
            return this;
        }


        public TicketPostDTOBuilder status(TicketStatus status) {
            this.status = status;
            return this;
        }

        public TicketPostDTO build() {
            TicketPostDTO ticketPostDTO = new TicketPostDTO();
            ticketPostDTO.setId(id);
            ticketPostDTO.setReservationNo(reservationNo);
            ticketPostDTO.setShow(show);
            ticketPostDTO.setPrice(price);
            ticketPostDTO.setCustomer(customer);
            ticketPostDTO.setSeat(seat);
            ticketPostDTO.setSector(sector);
            ticketPostDTO.setStatus(status);
            return ticketPostDTO;
        }
    }

}
