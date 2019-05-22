package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.artist;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.event.EventDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;


@ApiModel(value = "ArtistDTO")
public class ArtistDTO {

    @ApiModelProperty(name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(name = "The username of the artist")
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

    public static ArtistDTOBuilder builder() {
        return new ArtistDTOBuilder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArtistDTO artistDTO = (ArtistDTO) o;
        return id.equals(artistDTO.id) &&
            name.equals(artistDTO.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "ArtistDTO{" +
            "id=" + id +
            ", username='" + name + '\'' +
            '}';
    }

    public static final class ArtistDTOBuilder {

        private Long id;
        private String name;

        private ArtistDTOBuilder() {}

        public ArtistDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ArtistDTOBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ArtistDTO build() {
            ArtistDTO artist = new ArtistDTO();
            artist.setId(id);
            artist.setName(name);
            return artist;
        }
    }
}
