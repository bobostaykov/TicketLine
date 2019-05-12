package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

@Entity
public class Artist {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_artist_id")
    @SequenceGenerator(name = "seq_artist_id", sequenceName = "seq_artist_id")
    private Long id;

    @Column(nullable = false, unique = true)
    @Size(max = 64)
    private String name;

    @ManyToMany
    @JoinTable(name = "participation",
        joinColumns = {@JoinColumn(name = "artist_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "event_id", referencedColumnName = "id")})
    private List<Event> eventParticipations;

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

    public List<Event> getEventParticipations() {
        return eventParticipations;
    }

    public void setEventParticipations(List<Event> eventParticipations) {
        this.eventParticipations = eventParticipations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Artist artist = (Artist) o;

        if (id != null ? !id.equals(artist.id) : artist.id != null) return false;
        if (name != null ? !name.equals(artist.name) : artist.name != null) return false;
        return eventParticipations != null ? eventParticipations.equals(artist.eventParticipations) : artist.eventParticipations == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (eventParticipations != null ? eventParticipations.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Artist{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", eventParticipations=" + eventParticipations +
            '}';
    }

    public static ArtistBuilder builder() { return new ArtistBuilder(); }

    public static final class ArtistBuilder {

        private Long id;
        private String name;
        private List<Event> eventParticipations;

        private ArtistBuilder() {}

        public ArtistBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ArtistBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ArtistBuilder eventParticipations(List<Event> eventParticipations) {
            this.eventParticipations = eventParticipations;
            return this;
        }

        public Artist build() {
            Artist artist = new Artist();
            artist.setId(id);
            artist.setName(name);
            artist.setEventParticipations(eventParticipations);
            return artist;
        }
    }
}
