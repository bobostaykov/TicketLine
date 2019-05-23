package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.searchParameters;

import java.util.Objects;

public class EventSearchParametersDTO {
    private String name;
    private Integer durationInMinutes;
    private String content;
    private String artistName;

    public EventSearchParametersDTO(String name, Integer durationInMinutes, String content, String artistName) {
        this.name = name;
        this.durationInMinutes = durationInMinutes;
        this.content = content;
        this.artistName = artistName;
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

    @Override
    public String toString() {
        return "EventSearchParametersDTO{" +
            (name != null ? "name='" + name + '\'' : "") +
            (durationInMinutes != null ?", durationInMinutes="+ durationInMinutes : "") +
            (content != null ? ", content='" + content + '\'' : "") +
            (artistName != null ? ", artistName='" + artistName + '\'' : "") +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventSearchParametersDTO that = (EventSearchParametersDTO) o;
        return Objects.equals(name, that.name) &&
            Objects.equals(durationInMinutes, that.durationInMinutes) &&
            Objects.equals(content, that.content) &&
            Objects.equals(artistName, that.artistName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, durationInMinutes, content, artistName);
    }

    public static class EventSearchParametersDTOBuilder {
        private String name;
        private Integer durationInMinutes;
        private String content;
        private String artistName;

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

        public EventSearchParametersDTO build() {
            return new EventSearchParametersDTO(name, durationInMinutes, content, artistName);
        }
    }
}

