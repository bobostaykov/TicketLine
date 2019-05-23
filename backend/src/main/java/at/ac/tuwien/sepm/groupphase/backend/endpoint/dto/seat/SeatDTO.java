package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.seat;

import at.ac.tuwien.sepm.groupphase.backend.datatype.PriceCategory;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.hall.HallDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "SeatDTO", description = "DTO for seat entities")
public class SeatDTO {

    @ApiModelProperty(name = "The automatically generated database id of the seat")
    private Long id;

    @ApiModelProperty(name = "The seat number")
    private Integer seatNumber;

    @ApiModelProperty(name = "The seat row")
    private Integer seatRow;

    @ApiModelProperty(name = "The seat's price category. Either cheap, average or expensive.")
    private PriceCategory priceCategory;

    @ApiModelProperty(name = "Hall entity the seat belongs to")
    private HallDTO hallDTO;

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

    public HallDTO getHallDTO() {
        return hallDTO;
    }

    public void setHallDTO(HallDTO hallDTO) {
        this.hallDTO = hallDTO;
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
            ", priceCategory='" + priceCategory + "\'" +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return  true;
        if(o == null || getClass() != o.getClass()) return false;
        SeatDTO seatDTO = (SeatDTO) o;
        if(id != null ? ! id.equals(seatDTO.id) : seatDTO.id != null) return false;
        if(seatNumber != null ? ! seatNumber.equals(seatDTO.getSeatNumber()) : seatDTO.getSeatNumber() != null) return false;
        if(seatRow != null ? ! seatRow.equals(seatDTO.getSeatRow()) : seatDTO.getSeatRow() != null) return false;
        return priceCategory != null ? priceCategory.equals(seatDTO.getPriceCategory()) : seatDTO.getPriceCategory() == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (seatNumber != null ? seatNumber.hashCode() : 0);
        result = 31 * result + (seatRow != null ? seatRow.hashCode() : 0);
        result = 31 * result + (priceCategory != null ? priceCategory.hashCode() : 0);
        return result;
    }

    public static class SeatDTOBuilder {
        private Long id;
        private Integer seatNumber;
        private Integer seatRow;
        private PriceCategory priceCategory;
        private HallDTO hallDTO;

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

        public SeatDTOBuilder hallDTO(HallDTO hallDTO){
            this.hallDTO = hallDTO;
            return this;
        }

        public SeatDTO build(){
            SeatDTO seatDTO = new SeatDTO();
            seatDTO.setId(id);
            seatDTO.setSeatNumber(seatNumber);
            seatDTO.setSeatRow(seatRow);
            seatDTO.setPriceCategory(priceCategory);
            seatDTO.setHallDTO(hallDTO);
            return seatDTO;
        }
    }

    //TODO: incorporate halldto into equals hashcode and toString methods
}
