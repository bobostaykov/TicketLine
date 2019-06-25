package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.event;

import at.ac.tuwien.sepm.groupphase.backend.datatype.EventType;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.artist.ArtistDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

@ApiModel(value = "EventDTO")
public class EventDTO {

    @ApiModelProperty(name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(name = "The unique name of the event")
    @NotBlank
    private String name;

    @ApiModelProperty(name = "The type of the event (THEATER | OPER | FESTIVAL | CONCERT | MOVIE | MUSICAL)")
    private EventType eventType;

    @ApiModelProperty(name = "A description of the event")
    private String description;

    @ApiModelProperty(name = "A content of the event")
    private String content;

    @ApiModelProperty(name = "The duration of the Event")
    private Integer durationInMinutes;

    @ApiModelProperty(name = "The artist that participates in the event")
    private ArtistDTO artist;

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

    public ArtistDTO getArtist() {
        return artist;
    }

    public void setArtist(ArtistDTO artist) {
        this.artist = artist;
    }

    public Integer getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(Integer durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public static EventDTOBuilder builder() {
        return new EventDTOBuilder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventDTO eventDTO = (EventDTO) o;
        return Objects.equals(id, eventDTO.id) &&
            name.equals(eventDTO.name) &&
            eventType == eventDTO.eventType &&
            Objects.equals(description, eventDTO.description) &&
            Objects.equals(content, eventDTO.content) &&
            durationInMinutes.equals(eventDTO.durationInMinutes) &&
            artist.equals(eventDTO.artist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, eventType, description, content, durationInMinutes, artist);
    }

    @Override
    public String toString() {
        return "EventDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", eventType=" + eventType +
            ", description='" + description + '\'' +
            ", content='" + content + '\'' +
            ", durationInMinutes=" + durationInMinutes +
            ", artist=" + artist +
            '}';
    }

    public static final class EventDTOBuilder {
        private Long id;
        private String name;
        private EventType eventType;
        private String description;
        private String content;
        private ArtistDTO artist;
        private Integer durationInMinutes;

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

        public EventDTOBuilder artist(ArtistDTO artist) {
            this.artist = artist;
            return this;
        }

        public EventDTOBuilder durationInMinutes (Integer duration){
            this.durationInMinutes = duration;
            return this;
        }

        public EventDTO build() {
            EventDTO eventDTO = new EventDTO();
            eventDTO.setId(id);
            eventDTO.setName(name);
            eventDTO.setEventType(eventType);
            eventDTO.setDescription(description);
            eventDTO.setContent(content);
            eventDTO.setArtist(artist);
            eventDTO.setDurationInMinutes(durationInMinutes);
            return eventDTO;
        }
    }
}
