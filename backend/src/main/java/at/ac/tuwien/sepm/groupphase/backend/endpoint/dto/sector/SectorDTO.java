package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.sector;

import at.ac.tuwien.sepm.groupphase.backend.datatype.PriceCategory;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.hall.HallDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

@ApiModel(value = "SectorDTO", description = "A DTO for sector entities")
public class SectorDTO {

    @ApiModelProperty(name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(name = "The sector number")
    private Integer sectorNumber;

    @ApiModelProperty(name = "The sector's price category. Either cheap, average or expensive")
    private PriceCategory priceCategory;

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

    public static SectorDTOBuilder builder(){
        return new SectorDTOBuilder();
    }

    @Override
    public String toString() {
        return "SectorDTO{" +
            "id=" + id +
            ", sectorNumber=" + sectorNumber +
            ", priceCategory='" + priceCategory + "\'" +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return  true;
        if(o == null || getClass() != o.getClass()) return false;
        SectorDTO sectorDTO = (SectorDTO) o;
        return Objects.equals(id, sectorDTO.getId()) &&
            Objects.equals(sectorNumber, sectorDTO.getSectorNumber()) &&
            Objects.equals(priceCategory, sectorDTO.getPriceCategory());
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (sectorNumber != null ? sectorNumber.hashCode() : 0);
        result = 31 * result + (priceCategory != null ? priceCategory.hashCode() : 0);
        return result;
    }

    public static final class SectorDTOBuilder{
        private Long id;
        private Integer sectorNumber;
        private PriceCategory priceCategory;

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

        public SectorDTO build(){
            SectorDTO sectorDTO = new SectorDTO();
            sectorDTO.setId(id);
            sectorDTO.setSectorNumber(sectorNumber);
            sectorDTO.setPriceCategory(priceCategory);
            return sectorDTO;
        }
    }
}
