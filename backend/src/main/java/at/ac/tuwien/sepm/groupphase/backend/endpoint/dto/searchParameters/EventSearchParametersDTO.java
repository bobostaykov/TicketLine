package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.searchParameters;

import at.ac.tuwien.sepm.groupphase.backend.datatype.EventType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.util.Objects;

public class EventSearchParametersDTO {
    @NotBlank
    private String name;
    @Positive
    private Integer durationInMinutes;
    @NotEmpty
    private String content;
    @NotBlank
    private String artistName;
    @NotBlank
    private String description;
    private EventType eventType;

    public EventSearchParametersDTO(String name, Integer durationInMinutes, String content, String artistName, String description, EventType eventType) {
        this.name = name;
        this.durationInMinutes = durationInMinutes;
        this.content = content;
        this.artistName = artistName;
        this.description = description;
        this.eventType = eventType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(Integer durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static EventSearchParametersDTOBuilder builder(){return new EventSearchParametersDTOBuilder();}

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventSearchParametersDTO that = (EventSearchParametersDTO) o;
        return Objects.equals(name, that.name) &&
            Objects.equals(durationInMinutes, that.durationInMinutes) &&
            Objects.equals(content, that.content) &&
            Objects.equals(artistName, that.artistName) &&
            Objects.equals(description, that.description) &&
            eventType == that.eventType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, durationInMinutes, content, artistName, description, eventType);
    }

    @Override
    public String toString() {
        return "EventSearchParametersDTO{" +
            "name='" + name + '\'' +
            ", durationInMinutes=" + durationInMinutes +
            ", content='" + content + '\'' +
            ", artistName='" + artistName + '\'' +
            ", description='" + description + '\'' +
            ", eventType=" + eventType +
            '}';
    }

    public static class EventSearchParametersDTOBuilder {
        private String name;
        private Integer durationInMinutes;
        private String content;
        private String artistName;
        private String description;
        private EventType eventType;

        public  EventSearchParametersDTOBuilder(){}

        public EventSearchParametersDTOBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public EventSearchParametersDTOBuilder setDurationInMinutes(Integer durationInMinutes) {
            this.durationInMinutes = durationInMinutes;
            return this;
        }

        public EventSearchParametersDTOBuilder setContent(String content) {
            this.content = content;
            return this;
        }
        public EventSearchParametersDTOBuilder setArtistName(String artistName){
            this.artistName = artistName;
            return this;
        }

        public EventSearchParametersDTOBuilder setDescription(String description){
            this.description = description;
            return this;
        }

        public EventSearchParametersDTOBuilder setEventType (EventType eventType){
            this.eventType = eventType;
            return this;
        }

        public EventSearchParametersDTO build() {
            return new EventSearchParametersDTO(name, durationInMinutes, content, artistName, description, eventType);
        }
    }
}

