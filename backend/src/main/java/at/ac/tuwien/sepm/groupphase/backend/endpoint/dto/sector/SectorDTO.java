package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.sector;

import at.ac.tuwien.sepm.groupphase.backend.datatype.PriceCategory;
import at.ac.tuwien.sepm.groupphase.backend.datatype.TicketStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Objects;

@ApiModel(value = "SectorDTO", description = "A DTO for sector entities")
public class SectorDTO {

    @ApiModelProperty(name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(name = "The sector number", required = true)
    @NotNull(message = "Sector number was not set")
    @Positive(message = "Sector number was negative or zero")
    private Integer sectorNumber;

    @ApiModelProperty(name = "The sector's price category. Either cheap, average or expensive", required = true)
    @NotNull(message = "Sector price category was not set")
    private PriceCategory priceCategory;

    @ApiModelProperty(name = "The sector's ticket status declares whether the sector has already been sold or reserved")
    private TicketStatus ticketStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSectorNumber() {
        return sectorNumber;
    }

    public void setSectorNumber(Integer sectorNumber) {
        this.sectorNumber = sectorNumber;
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

    public static SectorDTOBuilder builder(){
        return new SectorDTOBuilder();
    }

    @Override
    public String toString() {
        return "SectorDTO{" +
            "id=" + id +
            ", sectorNumber=" + sectorNumber +
            ", priceCategory=" + priceCategory +
            ", ticketStatus=" + ticketStatus +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SectorDTO)) return false;
        SectorDTO sectorDTO = (SectorDTO) o;
        return Objects.equals(getId(), sectorDTO.getId()) &&
            Objects.equals(getSectorNumber(), sectorDTO.getSectorNumber()) &&
            getPriceCategory() == sectorDTO.getPriceCategory() &&
            Objects.equals(getTicketStatus(), sectorDTO.getTicketStatus());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getSectorNumber(), getPriceCategory(), getTicketStatus());
    }



    public static final class SectorDTOBuilder{
        private Long id;
        private Integer sectorNumber;
        private PriceCategory priceCategory;
        private TicketStatus ticketStatus;

        private SectorDTOBuilder(){}

        public SectorDTOBuilder id(Long id){
            this.id = id;
            return this;
        }

        public SectorDTOBuilder sectorNumber(Integer sectorNumber){
            this.sectorNumber = sectorNumber;
            return this;
        }

        public SectorDTOBuilder priceCategory(PriceCategory priceCategory){
            this.priceCategory = priceCategory;
            return this;
        }

        public SectorDTOBuilder ticketStatus(TicketStatus ticketStatus){
            this.ticketStatus = ticketStatus;
            return this;
        }

        public SectorDTO build(){
            SectorDTO sectorDTO = new SectorDTO();
            sectorDTO.setId(id);
            sectorDTO.setSectorNumber(sectorNumber);
            sectorDTO.setPriceCategory(priceCategory);
            sectorDTO.setTicketStatus(ticketStatus);
            return sectorDTO;
        }
    }
}
