package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

//the class is unfinished, add missing attributes
@Entity
public class Hall {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_hall_id")
    @SequenceGenerator(name = "seq_hall_id", sequenceName = "seq_hall_id")
    private Long id;

    @Column(length = 64)
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hall")
    private List<Show> shows;

    public Hall() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Show> getShows() {
        return shows;
    }

    public void setShows(List<Show> shows) {
        this.shows = shows;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hall hall = (Hall) o;
        return id.equals(hall.id) &&
            name.equals(hall.name) &&
            shows.equals(hall.shows);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, shows);
    }

    @Override
    public String toString() {
        return "Hall{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", shows=" + shows +
            '}';
    }
}
