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

    @Column(nullable = false)
    @Size(max = 64)
    private String firstname;

    @Column(nullable = false)
    @Size(max = 64)
    private String lastname;

    @Column(nullable = false, unique = true)
    @Size(max = 64)
    private String artistname;

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

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getArtistname() {
        return artistname;
    }

    public void setArtistname(String artistname) {
        this.artistname = artistname;
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
        return id.equals(artist.id) &&
            firstname.equals(artist.firstname) &&
            lastname.equals(artist.lastname) &&
            artistname.equals(artist.artistname) &&
            Objects.equals(eventParticipations, artist.eventParticipations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstname, lastname, artistname, eventParticipations);
    }

    @Override
    public String toString() {
        return "Artist{" +
            "id=" + id +
            ", firstname='" + firstname + '\'' +
            ", lastname='" + lastname + '\'' +
            ", artistname='" + artistname + '\'' +
            ", eventParticipations=" + eventParticipations +
            '}';
    }

    public static final class ArtistBuilder {

        private Long id;
        private String firstname;
        private String lastname;
        private String artistname;
        private List<Event> eventParticipations;

        private ArtistBuilder() {}

        public ArtistBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ArtistBuilder firstname(String firstname) {
            this.firstname = firstname;
            return this;
        }

        public ArtistBuilder lastname(String lastname) {
            this.lastname = lastname;
            return this;
        }

        public ArtistBuilder artistname(String artistname) {
            this.artistname = artistname;
            return this;
        }

        public ArtistBuilder eventParticipations(List<Event> eventParticipations) {
            this.eventParticipations = eventParticipations;
            return this;
        }

        public Artist build() {
            Artist artist = new Artist();
            artist.setId(id);
            artist.setFirstname(firstname);
            artist.setLastname(lastname);
            artist.setArtistname(artistname);
            artist.setEventParticipations(eventParticipations);
            return artist;
        }
    }
}
