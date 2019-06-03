package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ticket;

import at.ac.tuwien.sepm.groupphase.backend.datatype.TicketStatus;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.customer.CustomerDTO;
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

    @ApiModelProperty(name = "The seat number of this ticket")
    private Integer seatNumber;

    @ApiModelProperty(name = "The row number of this ticket")
    private Integer rowNumber;

    @ApiModelProperty(name = "The sector number of this ticket")
    private Integer sectorNumber;

    @ApiModelProperty(name = "Status of the ticket (RESERVATED, SOLD)")
    private TicketStatus status;

    public void setId(Long reservationNumber) {
        this.id = reservationNumber;
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

        TicketDTO ticketDTO = (TicketDTO) o;

        if (id != null ? !id.equals(ticketDTO.id) : ticketDTO.id != null) return false;
        if (show != null ? !show.equals(ticketDTO.show) : ticketDTO.show != null) return false;
        if (price != null ? !price.equals(ticketDTO.price) : ticketDTO.price != null) return false;
        if (customer != null ? !customer.equals(ticketDTO.customer) : ticketDTO.customer != null) return false;
        if (seatNumber != null ? !seatNumber.equals(ticketDTO.seatNumber) : ticketDTO.seatNumber != null) return false;
        if (rowNumber != null ? !rowNumber.equals(ticketDTO.rowNumber) : ticketDTO.rowNumber != null) return false;
        if (status != null ? !status.equals(ticketDTO.status) : ticketDTO.status != null) return false;
        return sectorNumber != null ? sectorNumber.equals(ticketDTO.sectorNumber) : ticketDTO.sectorNumber == null;
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

    public static final class TicketDTOBuilder {

        private Long id;
        private ShowDTO show;
        private Double price;
        private CustomerDTO customer;
        private Integer seatNumber;
        private Integer rowNumber;
        private Integer sectorNumber;
        private TicketStatus status;

        public TicketDTOBuilder id(Long reservationNumber) {
            this.id = reservationNumber;
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

        public TicketDTOBuilder seatNumber(Integer seatNumber) {
            this.seatNumber = seatNumber;
            return this;
        }

        public TicketDTOBuilder rowNumber(Integer rowNumber) {
            this.rowNumber = rowNumber;
            return this;
        }

        public TicketDTOBuilder sectorNumber(Integer sectorNumber) {
            this.sectorNumber = sectorNumber;
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
            ticketDTO.setSeatNumber(seatNumber);
            ticketDTO.setRowNumber(rowNumber);
            ticketDTO.setSectorNumber(sectorNumber);
            ticketDTO.setStatus(status);
            return ticketDTO;
        }
    }

}
