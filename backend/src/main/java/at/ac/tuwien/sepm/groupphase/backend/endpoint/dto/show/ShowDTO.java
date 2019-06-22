package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.show;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.event.EventDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.hall.HallDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.PricePattern;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@ApiModel(value = "ShowDTO")
public class ShowDTO {

    @ApiModelProperty(name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(name = "The event to which the show belongs")
    private EventDTO event;

    @Positive
    @ApiModelProperty(name = "The duration in minutes")
    private Integer durationInMinutes;

    @PositiveOrZero
    @ApiModelProperty(name = "The number of tickets sold for that show")
    private Long ticketsSold;

    @ApiModelProperty(name = "The date of the show")
    private LocalDate date;

    @ApiModelProperty(name = "The time of the show")
    private LocalTime time;

    @ApiModelProperty(name = "The hall in which the show takes place")
    private HallDTO hall;

    @ApiModelProperty(name = "A description of the show")
    private String description;

    @ApiModelProperty(name = "The Pattern of the prices")
    private PricePattern pricePattern;



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


    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
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

    public Long getTicketsSold() {
        return ticketsSold;
    }

    public void setTicketsSold(Long ticketsSold) {
        this.ticketsSold = ticketsSold;
    }

    public PricePattern getPricePattern() {
        return pricePattern;
    }

    public void setPricePattern(PricePattern pricePattern) {
        this.pricePattern = pricePattern;
    }

    public static ShowDTOBuilder builder() { return new ShowDTOBuilder(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShowDTO showDTO = (ShowDTO) o;
        return Objects.equals(id, showDTO.id) &&
            Objects.equals(event, showDTO.event) &&
            Objects.equals(ticketsSold, showDTO.ticketsSold) &&
            Objects.equals(date, showDTO.date) &&
            Objects.equals(time, showDTO.time) &&
            Objects.equals(hall, showDTO.hall) &&
            Objects.equals(description, showDTO.description) &&
            Objects.equals(pricePattern, showDTO.pricePattern);

    }

    @Override
    public int hashCode() {
        return Objects.hash(id, event, ticketsSold, date, time, hall, description);
    }

    public static final class ShowDTOBuilder {

        private Long id;
        private EventDTO event;
        private Long ticketsSold;
        private LocalDate date;
        private HallDTO hall;
        private String description;
        private LocalTime time;
        private PricePattern pricePattern;


        private ShowDTOBuilder() {}

        public ShowDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ShowDTOBuilder event(EventDTO event) {
            this.event = event;
            return this;
        }

        public ShowDTOBuilder ticketsSold(Long ticketsSold) {
            this.ticketsSold = ticketsSold;
            return this;
        }

        public ShowDTOBuilder date(LocalDate date) {
            this.date = date;
            return this;
        }

        public ShowDTOBuilder time(LocalTime time){
            this.time = time;
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
        public ShowDTOBuilder pricePattern(PricePattern pricePattern){
            this.pricePattern = pricePattern;
            return this;
        }



        public ShowDTO build() {
            ShowDTO showDTO = new ShowDTO();
            showDTO.setId(id);
            showDTO.setEvent(event);
            showDTO.setTicketsSold(ticketsSold);
            showDTO.setDate(date);
            showDTO.setTime(time);
            showDTO.setHall(hall);
            showDTO.setDescription(description);
            showDTO.setPricePattern(pricePattern);
            return showDTO;
        }
    }
}
