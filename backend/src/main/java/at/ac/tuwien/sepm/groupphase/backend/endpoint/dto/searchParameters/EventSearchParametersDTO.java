package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.searchParameters;

import at.ac.tuwien.sepm.groupphase.backend.datatype.EventType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.util.Objects;
@ApiModel(value = "EventSearchParametersDTO", description = "the parameters to search events for")
public class EventSearchParametersDTO {
    @NotBlank
    @ApiModelProperty(name = "the name of the event")
    private String name;
    @Positive
    @ApiModelProperty(name = "the duration of the event")
    private Integer durationInMinutes;
    @NotEmpty
    @ApiModelProperty(name = "the content of the event")
    private String content;
    @NotBlank
    @ApiModelProperty(name = "the name of the artist")
    private String artistName;
    @NotBlank
    @ApiModelProperty(name = "the description of the event")
    private String description;

    private EventType eventType;
    private String locationName;
    public EventSearchParametersDTO(String name, Integer durationInMinutes, String content, String artistName, String description, EventType eventType, String locationName) {
        this.name = name;
        this.durationInMinutes = durationInMinutes;
        this.content = content;
        this.artistName = artistName;
        this.description = description;
        this.eventType = eventType;
        this.locationName = locationName;
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

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
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
            eventType == that.eventType &&
            Objects.equals(locationName, that.locationName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, durationInMinutes, content, artistName, description, eventType, locationName);
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
            ", locationName='" + locationName + '\'' +
            '}';
    }

    public static class EventSearchParametersDTOBuilder {
        private String name;
        private String locationName;
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
        public EventSearchParametersDTOBuilder setLocationName (String locationName){
            this.locationName = locationName;
            return this;
        }

        public EventSearchParametersDTO build() {
            return new EventSearchParametersDTO(name, durationInMinutes, content, artistName, description, eventType, locationName);
        }
    }
}

