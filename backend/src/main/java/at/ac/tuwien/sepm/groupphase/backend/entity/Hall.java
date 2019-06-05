package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

//TODO: add properties determining price?
@Entity
public class Hall {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_hall_id")
    @SequenceGenerator(name = "seq_hall_id", sequenceName = "seq_hall_id")
    private Long id;

    @Column(nullable = false, length = 64)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "location_id")
    private Location location;

    @OneToMany(cascade = CascadeType.ALL,
        fetch = FetchType.LAZY,
        mappedBy = "hall")
    private List<Seat> seats;

    @OneToMany(cascade = CascadeType.ALL,
        fetch = FetchType.LAZY,
        mappedBy = "hall")
    private List<Sector> sectors;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public List<Sector> getSectors() {
        return sectors;
    }

    public void setSectors(List<Sector> sectors) {
        this.sectors = sectors;
    }

    public static HallBuilder builder() {
        return new HallBuilder();
    }

    public static final class HallBuilder {
        private Long id;
        private String name;
        private Location location;
        private List<Seat> seats;
        private List<Sector> sectors;

        private HallBuilder(){}

        public HallBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public HallBuilder name(String name){
            this.name = name;
            return this;
        }

        public HallBuilder location(Location location){
            this.location = location;
            return this;
        }

        public HallBuilder seats(List<Seat> seats) {
            this.seats = seats;
            return this;
        }

        public HallBuilder sectors(List<Sector> sectors) {
            this.sectors = sectors;
            return this;
        }

        public Hall build() {
            Hall hall = new Hall();
            hall.setId(id);
            hall.setName(name);
            hall.setLocation(location);
            hall.setSeats(seats);
            hall.setSectors(sectors);
            return hall;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Hall hall = (Hall) o;
            return Objects.equals(id, hall.getId()) &&
                Objects.equals(name, hall.getName()) &&
                Objects.equals(location, hall.getLocation()) &&
                Objects.equals(seats, hall.getSeats()) &&
                Objects.equals(sectors, hall.getSectors());
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name, location, seats, sectors);
        }

        @Override
        public String toString() {
            return "hall{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location=" + location +
                ", seats=" + seats +
                ", sectors=" + sectors +
                '}';
        }
    }
}
