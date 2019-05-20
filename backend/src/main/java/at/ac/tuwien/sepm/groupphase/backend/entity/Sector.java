package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.datatype.PriceCategory;

import javax.persistence.*;

@Entity
public class Sector {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_sector_id")
    @SequenceGenerator(name = "seq_sector_id", sequenceName = "seq_sector_id")
    private Integer id;

    @Column(nullable = false)
    private Integer sectorNumber;

    @Column(nullable = false)
    private PriceCategory priceCategory;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public static SectorBuilder builder(){
        return new SectorBuilder();
    }

    @Override
    public String toString() {
        return "Sector{" +
            "id=" + id +
            ", sectorNumber=" + sectorNumber +
            ", priceCategory='" + priceCategory + "\'" +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return  true;
        if(o == null || getClass() != o.getClass()) return false;
        Sector sector = (Sector) o;
        if(id != null ? ! id.equals(sector.id) : sector.id != null) return false;
        if(sectorNumber != null ? ! sectorNumber.equals(sector.getSectorNumber()) : sector.getSectorNumber() != null) return false;
        return priceCategory != null ? priceCategory.equals(sector.getPriceCategory()) : sector.getPriceCategory() == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (sectorNumber != null ? sectorNumber.hashCode() : 0);
        result = 31 * result + (priceCategory != null ? priceCategory.hashCode() : 0);
        return result;
    }

    public static final class SectorBuilder{
        private Integer id;
        private Integer sectorNumber;
        private PriceCategory priceCategory;
        private Long hallId;

        private SectorBuilder(){}

        public SectorBuilder id(Integer id){
            this.id = id;
            return this;
        }

        public SectorBuilder sectorNumber(Integer sectorNumber){
            this.sectorNumber = sectorNumber;
            return this;
        }

        public SectorBuilder priceCategory(PriceCategory priceCategory){
            this.priceCategory = priceCategory;
            return this;
        }

        public SectorBuilder hallId(Long hallId){
            this.hallId = hallId;
            return this;
        }

        public Sector build(){
            Sector sector = new Sector();
            sector.setId(id);
            sector.setSectorNumber(sectorNumber);
            sector.setPriceCategory(priceCategory);
            return sector;
        }
    }
}
