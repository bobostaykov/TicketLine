package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.datatype.PriceCategory;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Sector {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_sector_id")
    @SequenceGenerator(name = "seq_sector_id", sequenceName = "seq_sector_id")
    private Long id;

    @Column(nullable = false)
    private Integer sectorNumber;

    @Column(nullable = false)
    private PriceCategory priceCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hall_id", nullable = false)
    private Hall hall;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sector")
    private List<Ticket> tickets;

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

    public static SectorBuilder builder(){
        return new SectorBuilder();
    }

    public Hall getHall() {
        return hall;
    }

    public void setHall(Hall hall) {
        this.hall = hall;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> ticketDTOS) {
        this.tickets = ticketDTOS;
    }

    @Override
    public String toString() {
        return "Sector{" +
            "id=" + id +
            ", sectorNumber=" + sectorNumber +
            ", priceCategory=" + priceCategory +
            ", hall=" + hall +
            ", tickets=" + tickets +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sector)) return false;
        Sector sector = (Sector) o;
        return Objects.equals(getId(), sector.getId()) &&
            Objects.equals(getSectorNumber(), sector.getSectorNumber()) &&
            getPriceCategory() == sector.getPriceCategory() &&
            Objects.equals(getHall(), sector.getHall()) &&
            Objects.equals(getTickets(), sector.getTickets());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getSectorNumber(), getPriceCategory(), getHall(), getTickets());
    }

    public static final class SectorBuilder{
        private Long id;
        private Integer sectorNumber;
        private PriceCategory priceCategory;
        private Hall hall;
        private List<Ticket> tickets;

        private SectorBuilder(){}

        public SectorBuilder id(Long id){
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

        public SectorBuilder hall(Hall hall){
            this.hall = hall;
            return this;
        }

        public SectorBuilder tickets(List<Ticket> tickets){
            this.tickets = tickets;
            return this;
        }

        public Sector build(){
            Sector sector = new Sector();
            sector.setId(id);
            sector.setSectorNumber(sectorNumber);
            sector.setPriceCategory(priceCategory);
            sector.setHall(hall);
            sector.setTickets(tickets);
            return sector;
        }
    }
}
