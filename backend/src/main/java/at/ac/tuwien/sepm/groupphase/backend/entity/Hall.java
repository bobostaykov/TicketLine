package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.*;
import java.util.List;

//TODO add missing attributes
@Entity
public class Hall {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_hall_id")
    @SequenceGenerator(name = "seq_hall_id", sequenceName = "seq_hall_id")
    private Long id;

    @Column(nullable = false, length = 64)
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hall")
    private List<Show> shows;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "location_id")
    private Location location;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Show> getShows() {
        return shows;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setShows(List<Show> shows) {
        this.shows = shows;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public static HallBuilder builder() {
        return new HallBuilder();
    }

    public static final class HallBuilder{
        private Long id;
        private String name;
        private List<Show> shows;
        private Location location;

        private HallBuilder(){}

        public HallBuilder id(Long id){
            this.id = id;
            return this;
        }

        public HallBuilder name(String name){
            this.name = name;
            return this;
        }
        public HallBuilder shows(List<Show> shows){
            this.shows = shows;
            return this;
        }
        public HallBuilder location(Location location){
            this.location = location;
            return this;
        }

        public Hall build(){
            Hall hall = new Hall();
            hall.setId(id);
            hall.setName(name);
            hall.setShows(shows);
            hall.setLocation(location);
            return hall;
        }

    }

}
