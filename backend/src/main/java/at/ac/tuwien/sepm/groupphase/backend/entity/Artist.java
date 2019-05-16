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
    private String name;

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

    public static ArtistBuilder builder() {
        return new ArtistBuilder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artist artist = (Artist) o;
        return id.equals(artist.id) &&
            name.equals(artist.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public static final class ArtistBuilder {

        private Long id;
        private String name;

        private ArtistBuilder() {}

        public ArtistBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ArtistBuilder name(String name) {
            this.name = name;
            return this;
        }

        public Artist build() {
            Artist artist = new Artist();
            artist.setId(id);
            artist.setName(name);
            return artist;
        }
    }
}
