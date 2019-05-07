package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.event;

import at.ac.tuwien.sepm.groupphase.backend.datatype.EventType;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.artist.ArtistDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.show.ShowDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.HashSet;
import java.util.Objects;

@ApiModel(value = "EventDTO")
public class EventDTO {

    @ApiModelProperty(name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(name = "The unique name of the event")
    private String name;

    @ApiModelProperty(name = "The type of the event (THEATER | OPER | FESTIVAL | CONCERT | MOVIE | MUSICAL)")
    private EventType eventType;

    @ApiModelProperty(name = "A description of the event")
    private String description;

    @ApiModelProperty(name = "A content of the event")
    private String content;

    @ApiModelProperty(name = "The artists that participate in the event")
    private HashSet<ArtistDTO> participatingArtists;

    @ApiModelProperty(name = "The shows from which the event consists")
    private HashSet<ShowDTO> shows;

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

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public HashSet<ArtistDTO> getParticipatingArtists() {
        return participatingArtists;
    }

    public void setParticipatingArtists(HashSet<ArtistDTO> participatingArtists) {
        this.participatingArtists = participatingArtists;
    }

    public HashSet<ShowDTO> getShows() {
        return shows;
    }

    public void setShows(HashSet<ShowDTO> shows) {
        this.shows = shows;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventDTO eventDTO = (EventDTO) o;
        return id.equals(eventDTO.id) &&
            name.equals(eventDTO.name) &&
            eventType == eventDTO.eventType &&
            Objects.equals(description, eventDTO.description) &&
            Objects.equals(content, eventDTO.content) &&
            Objects.equals(participatingArtists, eventDTO.participatingArtists) &&
            Objects.equals(shows, eventDTO.shows);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, eventType, description, content, participatingArtists, shows);
    }

    @Override
    public String toString() {
        return "EventDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", eventType=" + eventType +
            ", description='" + description + '\'' +
            ", content='" + content + '\'' +
            ", participatingArtists=" + participatingArtists +
            ", shows=" + shows +
            '}';
    }

    public static final class EventDTOBuilder {
        private Long id;
        private String name;
        private EventType eventType;
        private String description;
        private String content;
        private HashSet<ShowDTO> shows;
        private HashSet<ArtistDTO> participatingArtists;

        private EventDTOBuilder() {}

        public EventDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public EventDTOBuilder name(String name) {
            this.name = name;
            return this;
        }

        public EventDTOBuilder eventType(EventType eventType) {
            this.eventType = eventType;
            return this;
        }

        public EventDTOBuilder description(String description) {
            this.description = description;
            return this;
        }

        public EventDTOBuilder content(String content) {
            this.content = content;
            return this;
        }

        public EventDTOBuilder shows(HashSet<ShowDTO> shows) {
            this.shows = shows;
            return this;
        }

        public EventDTOBuilder participatingArtists(HashSet<ArtistDTO> participatingArtists) {
            this.participatingArtists = participatingArtists;
            return this;
        }

        public EventDTO build() {
            EventDTO eventDTO = new EventDTO();
            eventDTO.setId(id);
            eventDTO.setName(name);
            eventDTO.setEventType(eventType);
            eventDTO.setDescription(description);
            eventDTO.setContent(content);
            eventDTO.setParticipatingArtists(participatingArtists);
            eventDTO.setShows(shows);
            return eventDTO;
        }
    }
}
