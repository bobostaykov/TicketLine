package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.seat;

import at.ac.tuwien.sepm.groupphase.backend.datatype.PriceCategory;
import at.ac.tuwien.sepm.groupphase.backend.datatype.TicketStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Objects;

@ApiModel(value = "SeatDTO", description = "DTO for seat entities")
@Validated
public class SeatDTO {

    @ApiModelProperty(name = "The automatically generated database id of the seat")
    private Long id;

    @ApiModelProperty(name = "The seat number", required = true)
    @NotNull(message = "Seat Number was not set")
    @Positive(message = "Seat Number was negative or 0")
    private Integer seatNumber;

    @ApiModelProperty(name = "The seat row", required = true)
    @NotNull(message = "Seat Row was not set")
    @Positive(message = "Seat Row was negative or 0")
    private Integer seatRow;

    @ApiModelProperty(name = "The seat's price category. Either cheap, average or expensive.", required = true)
    @NotNull(message = "Seat Price Category was not set")
    private PriceCategory priceCategory;

    @ApiModelProperty(name = "The seat's ticket status declares whether a seat has already been sold or reserved")
    private TicketStatus ticketStatus;

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

    public TicketStatus getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(TicketStatus ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public static SeatDTOBuilder builder(){
        return new SeatDTOBuilder();
    }

    @Override
    public String toString() {
        return "SeatDTO{" +
            "id=" + id +
            ", seatNumber=" + seatNumber +
            ", seatRow=" + seatRow +
            ", priceCategory=" + priceCategory +
            ", ticketStatus=" + ticketStatus +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SeatDTO)) return false;
        SeatDTO seatDTO = (SeatDTO) o;
        return Objects.equals(getId(), seatDTO.getId()) &&
            Objects.equals(getSeatNumber(), seatDTO.getSeatNumber()) &&
            Objects.equals(getSeatRow(), seatDTO.getSeatRow()) &&
            Objects.equals(getPriceCategory(), seatDTO.getPriceCategory()) &&
            Objects.equals(getTicketStatus(), seatDTO.getTicketStatus());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getSeatNumber(), getSeatRow(), getPriceCategory(), getTicketStatus());
    }



    public static class SeatDTOBuilder {
        private Long id;
        private Integer seatNumber;
        private Integer seatRow;
        private PriceCategory priceCategory;
        private TicketStatus ticketStatus;

        private SeatDTOBuilder(){}

        public SeatDTOBuilder id(Long id){
            this.id = id;
            return this;
        }

        public SeatDTOBuilder seatNumber(Integer seatNumber){
            this.seatNumber = seatNumber;
            return this;
        }

        public SeatDTOBuilder seatRow(Integer seatRow){
            this.seatRow = seatRow;
            return this;
        }

        public SeatDTOBuilder priceCategory(PriceCategory priceCategory){
            this.priceCategory = priceCategory;
            return this;
        }

        public SeatDTOBuilder ticketStatus(TicketStatus ticketStatus) {
            this.ticketStatus = ticketStatus;
            return this;
        }

        public SeatDTO build(){
            SeatDTO seatDTO = new SeatDTO();
            seatDTO.setId(id);
            seatDTO.setSeatNumber(seatNumber);
            seatDTO.setSeatRow(seatRow);
            seatDTO.setPriceCategory(priceCategory);
            seatDTO.setTicketStatus(ticketStatus);
            return seatDTO;
        }
    }
}
