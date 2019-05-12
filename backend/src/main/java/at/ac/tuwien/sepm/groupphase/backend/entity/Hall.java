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
}
