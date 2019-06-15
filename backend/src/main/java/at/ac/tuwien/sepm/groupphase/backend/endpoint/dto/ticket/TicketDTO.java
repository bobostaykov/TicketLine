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

    @ApiModelProperty(name = "The show this tickets was issued for")
    private ShowDTO show;

    @ApiModelProperty(name = "Price of the ticket")
    private Double price;

    @ApiModelProperty(name = "The customer this ticket was issued for")
    private CustomerDTO customer;

    @ApiModelProperty(name = "The seat this ticket was bought for")
    private SeatDTO seatDTO;

    @ApiModelProperty(name = "The sector this ticket was bought for")
    private SectorDTO sectorDTO;

    @ApiModelProperty(name = "Status of the ticket (RESERVATED, SOLD)")
    private TicketStatus status;

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

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public SeatDTO getSeatDTO() {
        return seatDTO;
    }

    public void setSeatDTO(SeatDTO seatDTO) {
        this.seatDTO = seatDTO;
    }

    public SectorDTO getSectorDTO() {
        return sectorDTO;
    }

    public void setSectorDTO(SectorDTO sectorDTO) {
        this.sectorDTO = sectorDTO;
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
        if (seatDTO != null) {
            out += ", seatNumber=" + seatDTO.toString();
        }
        if (sectorDTO != null) {
            out += ", rowNumber =" + sectorDTO.toString();
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
        if (seatDTO != null ? !seatDTO.equals(ticketDTO.seatDTO) : ticketDTO.seatDTO != null) return false;
        if (status != null ? !status.equals(ticketDTO.status) : ticketDTO.status != null) return false;
        return sectorDTO != null ? sectorDTO.equals(ticketDTO.sectorDTO) : ticketDTO.sectorDTO == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (show != null ? show.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (customer != null ? customer.hashCode() : 0);
        result = 31 * result + (seatDTO != null ? seatDTO.hashCode() : 0);
        result = 31 * result + (sectorDTO != null ? sectorDTO.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }


    public static final class TicketDTOBuilder {

        private Long id;
        private ShowDTO show;
        private Double price;
        private CustomerDTO customer;
        private SeatDTO seatDTO;
        private SectorDTO sectorDTO;
        private TicketStatus status;

        public TicketDTOBuilder id(Long id) {
            this.id = id;
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

        public TicketDTOBuilder seatDTO(SeatDTO seatDTO) {
            this.seatDTO = seatDTO;
            return this;
        }

        public TicketDTOBuilder sectorDTO(SectorDTO sectorDTO) {
            this.sectorDTO = sectorDTO;
            return this;
        }

        public TicketDTOBuilder status(TicketStatus status) {
            this.status = status;
            return this;
        }

        public TicketDTO build() {
            TicketDTO ticketDTO = new TicketDTO();
            ticketDTO.setId(id);
            ticketDTO.setShow(show);
            ticketDTO.setPrice(price);
            ticketDTO.setCustomer(customer);
            ticketDTO.setSeatDTO(seatDTO);
            ticketDTO.setSectorDTO(sectorDTO);
            ticketDTO.setStatus(status);
            return ticketDTO;
        }
    }

}
