package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ticket;

import at.ac.tuwien.sepm.groupphase.backend.datatype.TicketStatus;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.customer.CustomerDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.seat.SeatDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.sector.SectorDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.show.ShowDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(value = "TicketDTO", description = "A DTO for ticket entries via rest")
public class TicketDTO {

    @ApiModelProperty(name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(name = "Reservation number for ticket reservations")
    private String reservationNo;

    @ApiModelProperty(name = "The show this tickets was issued for")
    private ShowDTO show;

    @ApiModelProperty(name = "Price of the ticket")
    private Double price;

    @ApiModelProperty(name = "The customer this ticket was issued for")
    private CustomerDTO customer;

    @ApiModelProperty(name = "The seat number of this ticket")
    private SeatDTO seat;

    @ApiModelProperty(name = "The sector number of this ticket")
    private SectorDTO sector;

    @ApiModelProperty(name = "Status of the ticket (RESERVED, SOLD, EXPIRED)")
    private TicketStatus status;

    public String getReservationNo() {
        return reservationNo;
    }

    public void setReservationNo(String reservationNo) {
        this.reservationNo = reservationNo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setShow(ShowDTO show) {
        this.show = show;
    }

    public ShowDTO getShow() {
        return show;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPrice() {
        return price;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setSeat(SeatDTO seat) {
        this.seat = seat;
    }

    public SeatDTO getSeat() {
        return seat;
    }

    public void setSector(SectorDTO sector) {
        this.sector = sector;
    }

    public SectorDTO getSector() {
        return sector;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public static TicketDTOBuilder builder() {
        return new TicketDTOBuilder();
    }

    @Override
    public String toString() {
        String out = "TicketDTO{" +
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

        TicketDTO ticketDTO = (TicketDTO) o;

        if (id != null ? !id.equals(ticketDTO.id) : ticketDTO.id != null) return false;
        if (show != null ? !show.equals(ticketDTO.show) : ticketDTO.show != null) return false;
        if (price != null ? !price.equals(ticketDTO.price) : ticketDTO.price != null) return false;
        if (customer != null ? !customer.equals(ticketDTO.customer) : ticketDTO.customer != null) return false;
        if (seat != null ? !seat.equals(ticketDTO.seat) : ticketDTO.seat != null) return false;
        if (status != null ? !status.equals(ticketDTO.status) : ticketDTO.status != null) return false;
        if (reservationNo != null ? !reservationNo.equals(ticketDTO.reservationNo) : ticketDTO.reservationNo != null) return false;
        return sector != null ? sector.equals(ticketDTO.sector) : ticketDTO.sector == null;
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

    public static final class TicketDTOBuilder {

        private Long id;
        private String reservationNo;
        private ShowDTO show;
        private Double price;
        private CustomerDTO customer;
        private SeatDTO seat;
        private SectorDTO sector;
        private TicketStatus status;

        public TicketDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public TicketDTOBuilder reservationNo(String reservationNo) {
            this.reservationNo = reservationNo;
            return this;
        }

        public TicketDTOBuilder show(ShowDTO show) {
            this.show = show;
            return this;
        }

        public TicketDTOBuilder price(Double price) {
            this.price = price;
            return this;
        }
        public TicketDTOBuilder customer(CustomerDTO customer) {
            this.customer = customer;
            return this;
        }

        public TicketDTOBuilder seat(SeatDTO seat) {
            this.seat = seat;
            return this;
        }

        public TicketDTOBuilder sector(SectorDTO sector) {
            this.sector = sector;
            return this;
        }


        public TicketDTOBuilder status(TicketStatus status) {
            this.status = status;
            return this;
        }

        public TicketDTO build() {
            TicketDTO ticketDTO = new TicketDTO();
            ticketDTO.setId(id);
            ticketDTO.setReservationNo(reservationNo);
            ticketDTO.setShow(show);
            ticketDTO.setPrice(price);
            ticketDTO.setCustomer(customer);
            ticketDTO.setSeat(seat);
            ticketDTO.setSector(sector);
            ticketDTO.setStatus(status);
            return ticketDTO;
        }
    }

}
