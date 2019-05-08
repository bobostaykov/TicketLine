package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.show;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.event.EventDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.hall.HallDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;
import java.util.Objects;

@ApiModel(value = "ShowDTO")
public class ShowDTO {

    @ApiModelProperty(name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(name = "The event to which the show belongs")
    private EventDTO event;

    @ApiModelProperty(name = "The date and time of the show")
    private LocalDateTime dateTime;

    @ApiModelProperty(name = "The hall in which the show takes place")
    private HallDTO hall;

    @ApiModelProperty(name = "A description of the show")
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EventDTO getEvent() {
        return event;
    }

    public void setEvent(EventDTO event) {
        this.event = event;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public HallDTO getHall() {
        return hall;
    }

    public void setHall(HallDTO hall) {
        this.hall = hall;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShowDTO showDTO = (ShowDTO) o;
        return id.equals(showDTO.id) &&
            event.equals(showDTO.event) &&
            dateTime.equals(showDTO.dateTime) &&
            hall.equals(showDTO.hall) &&
            Objects.equals(description, showDTO.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, event, dateTime, hall, description);
    }

    @Override
    public String toString() {
        return "ShowDTO{" +
            "id=" + id +
            ", event=" + event +
            ", dateTime=" + dateTime +
            ", hall=" + hall +
            ", description='" + description + '\'' +
            '}';
    }

    public static final class ShowDTOBuilder {

        private Long id;
        private EventDTO event;
        private LocalDateTime dateTime;
        private HallDTO hall;
        private String description;

        private ShowDTOBuilder() {}

        public ShowDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ShowDTOBuilder event(EventDTO event) {
            this.event = event;
            return this;
        }

        public ShowDTOBuilder dateTime(LocalDateTime dateTime) {
            this.dateTime = dateTime;
            return this;
        }

        public ShowDTOBuilder hall(HallDTO hall) {
            this.hall = hall;
            return this;
        }

        public ShowDTOBuilder description(String description) {
            this.description = description;
            return this;
        }

        public ShowDTO build() {
            ShowDTO showDTO = new ShowDTO();
            showDTO.setId(id);
            showDTO.setEvent(event);
            showDTO.setDateTime(dateTime);
            showDTO.setHall(hall);
            showDTO.setDescription(description);
            return showDTO;
        }
    }
}