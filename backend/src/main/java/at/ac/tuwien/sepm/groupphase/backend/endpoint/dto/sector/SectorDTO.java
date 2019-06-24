package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.sector;

import at.ac.tuwien.sepm.groupphase.backend.datatype.PriceCategory;
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

    @ApiModelProperty(name = "Maximum capacity of customers for this sector per show", required = true)
    @NotNull(message = "Sector capacity was not set")
    @Positive(message = "Sector capacity must be greater than zero")
    private Integer maxCapacity;

    @ApiModelProperty(name = "Number of tickets already sold for this sector for a specific currently selected show")
    private Integer ticketsSold;

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

    public Integer getTicketsSold() {
        return ticketsSold;
    }

    public void setTicketsSold(Integer ticketsSold) {
        this.ticketsSold = ticketsSold;
    }

    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public static SectorDTOBuilder builder() {
        return new SectorDTOBuilder();
    }

    @Override
    public String toString() {
        return "SectorDTO{" +
            "id=" + id +
            ", sectorNumber=" + sectorNumber +
            ", priceCategory=" + priceCategory +
            ", maxCapacity=" + maxCapacity +
            ", ticketsSold=" + ticketsSold +
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
            Objects.equals(getMaxCapacity(), sectorDTO.getMaxCapacity()) &&
            Objects.equals(getTicketsSold(), sectorDTO.getTicketsSold());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getSectorNumber(), getPriceCategory(), getMaxCapacity(), getTicketsSold());
    }

    public static final class SectorDTOBuilder {
        private Long id;
        private Integer sectorNumber;
        private PriceCategory priceCategory;
        private Integer maxCapacity;
        private Integer ticketsSold;

        private SectorDTOBuilder() {
        }

        public SectorDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public SectorDTOBuilder sectorNumber(Integer sectorNumber) {
            this.sectorNumber = sectorNumber;
            return this;
        }

        public SectorDTOBuilder priceCategory(PriceCategory priceCategory) {
            this.priceCategory = priceCategory;
            return this;
        }

        public SectorDTOBuilder maxCapacity(Integer maxCapacity) {
            this.maxCapacity = maxCapacity;
            return this;
        }

        public SectorDTOBuilder ticketsSold(Integer ticketsSold) {
            this.ticketsSold = ticketsSold;
            return this;
        }

        public SectorDTO build() {
            SectorDTO sectorDTO = new SectorDTO();
            sectorDTO.setId(id);
            sectorDTO.setSectorNumber(sectorNumber);
            sectorDTO.setPriceCategory(priceCategory);
            sectorDTO.setMaxCapacity(maxCapacity);
            sectorDTO.setTicketsSold(ticketsSold);
            return sectorDTO;
        }
    }
}
