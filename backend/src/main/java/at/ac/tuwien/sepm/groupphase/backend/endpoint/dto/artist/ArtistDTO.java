package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.artist;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.event.EventDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.HashSet;
import java.util.Objects;


@ApiModel(value = "ArtistDTO")
public class ArtistDTO {

    @ApiModelProperty(name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(name = "The automatically generated database id")
    private String firstname;

    @ApiModelProperty(name = "The automatically generated database id")
    private String lastname;

    @ApiModelProperty(name = "The automatically generated database id")
    private String artistname;

    @ApiModelProperty(name = "The automatically generated database id")
    private HashSet<EventDTO> eventParticipations;

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

    public HashSet<EventDTO> getEventParticipations() {
        return eventParticipations;
    }

    public void setEventParticipations(HashSet<EventDTO> eventParticipations) {
        this.eventParticipations = eventParticipations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArtistDTO artistDTO = (ArtistDTO) o;
        return id.equals(artistDTO.id) &&
            firstname.equals(artistDTO.firstname) &&
            lastname.equals(artistDTO.lastname) &&
            artistname.equals(artistDTO.artistname) &&
            Objects.equals(eventParticipations, artistDTO.eventParticipations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstname, lastname, artistname, eventParticipations);
    }


    @Override
    public String toString() {
        return "ArtistDTO{" +
            "id=" + id +
            ", firstname='" + firstname + '\'' +
            ", lastname='" + lastname + '\'' +
            ", artistname='" + artistname + '\'' +
            ", eventParticipations=" + eventParticipations +
            '}';
    }

    public static final class ArtistDTOBuilder {

        private Long id;
        private String firstname;
        private String lastname;
        private String artistname;
        private HashSet<EventDTO> eventParticipations;

        private ArtistDTOBuilder() {}

        public ArtistDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ArtistDTOBuilder firstname(String firstname) {
            this.firstname = firstname;
            return this;
        }

        public ArtistDTOBuilder lastname(String lastname) {
            this.lastname = lastname;
            return this;
        }

        public ArtistDTOBuilder artistname(String artistname) {
            this.artistname = artistname;
            return this;
        }

        public ArtistDTOBuilder eventParticipations(HashSet<EventDTO> eventParticipations) {
            this.eventParticipations = eventParticipations;
            return this;
        }

        public ArtistDTO build() {
            ArtistDTO artist = new ArtistDTO();
            artist.setId(id);
            artist.setFirstname(firstname);
            artist.setLastname(lastname);
            artist.setArtistname(artistname);
            artist.setEventParticipations(eventParticipations);
            return artist;
        }
    }
}
