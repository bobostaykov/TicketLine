package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.artist;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.event.EventDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.message.DetailedMessageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Objects;


@ApiModel(value = "ArtistDTO")
public class ArtistDTO {

    @ApiModelProperty(name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(name = "The artist name under which the person introduces himself or herself")
    private String name;

    @ApiModelProperty(name = "The events in which the artists has or will participate")
    private List<EventDTO> eventParticipations;

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

    public List<EventDTO> getEventParticipations() {
        return eventParticipations;
    }

    public void setEventParticipations(List<EventDTO> eventParticipations) {
        this.eventParticipations = eventParticipations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ArtistDTO artistDTO = (ArtistDTO) o;

        if (id != null ? !id.equals(artistDTO.id) : artistDTO.id != null) return false;
        if (name != null ? !name.equals(artistDTO.name) : artistDTO.name != null) return false;
        return eventParticipations != null ? eventParticipations.equals(artistDTO.eventParticipations) : artistDTO.eventParticipations == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (eventParticipations != null ? eventParticipations.hashCode() : 0);
        return result;
    }

    public static ArtistDTOBuilder builder() { return new ArtistDTOBuilder(); }


    @Override
    public String toString() {
        return "ArtistDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", eventParticipations=" + eventParticipations +
            '}';
    }

    public static final class ArtistDTOBuilder {

        private Long id;
        private String name;
        private List<EventDTO> eventParticipations;

        private ArtistDTOBuilder() {}

        public ArtistDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ArtistDTOBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ArtistDTOBuilder eventParticipations(List<EventDTO> eventParticipations) {
            this.eventParticipations = eventParticipations;
            return this;
        }

        public ArtistDTO build() {
            ArtistDTO artist = new ArtistDTO();
            artist.setId(id);
            artist.setName(name);
            artist.setEventParticipations(eventParticipations);
            return artist;
        }
    }
}
