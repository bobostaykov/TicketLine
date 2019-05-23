package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.searchParameters;

import java.time.LocalDate;
import java.time.LocalTime;

public class ShowSearchParametersDTO {
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private LocalTime timeFrom;
    private LocalTime timeTo;
    private Integer priceInEuroFrom;
    private Integer priceInEuroTo;
    private String eventName;
    private String hallName;
    private Integer durationInMinutes;

    public ShowSearchParametersDTO(LocalDate dateFrom, LocalDate dateTo, LocalTime timeFrom, LocalTime timeTo, Integer priceInEuroFrom, Integer priceInEuroTo, String eventName, String hallName, Integer durationInMinutes) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.priceInEuroFrom = priceInEuroFrom;
        this.priceInEuroTo = priceInEuroTo;
        this.eventName = eventName;
        this.hallName = hallName;
        this.durationInMinutes = durationInMinutes;
    }

    public Integer getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(Integer durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }

    public LocalTime getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(LocalTime timeFrom) {
        this.timeFrom = timeFrom;
    }

    public LocalTime getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(LocalTime timeTo) {
        this.timeTo = timeTo;
    }

    public Integer getPriceInEuroFrom() {
        return priceInEuroFrom;
    }

    public void setPriceInEuroFrom(Integer priceInEuroFrom) {
        this.priceInEuroFrom = priceInEuroFrom;
    }

    public Integer getPriceInEuroTo() {
        return priceInEuroTo;
    }

    public void setPriceInEuroTo(Integer priceInEuroTo) {
        this.priceInEuroTo = priceInEuroTo;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getHallName() {
        return hallName;
    }

    public void setHallName(String hallName) {
        this.hallName = hallName;
    }

    public ShowSearchParametersDTOBuilder builder(){return new ShowSearchParametersDTOBuilder();}

    public static class ShowSearchParametersDTOBuilder {
        private LocalDate dateFrom;
        private LocalDate dateTo;
        private LocalTime timeFrom;
        private LocalTime timeTo;
        private Integer priceInEuroFrom;
        private Integer priceInEuroTo;
        private String eventName;
        private String hallName;
        private Integer durationInMinutes;

        public ShowSearchParametersDTOBuilder(){};

        public ShowSearchParametersDTOBuilder setDateFrom(LocalDate dateFrom) {
            this.dateFrom = dateFrom;
            return this;
        }

        public ShowSearchParametersDTOBuilder setDateTo(LocalDate dateTo) {
            this.dateTo = dateTo;
            return this;
        }

        public ShowSearchParametersDTOBuilder setTimeFrom(LocalTime timeFrom) {
            this.timeFrom = timeFrom;
            return this;
        }

        public ShowSearchParametersDTOBuilder setTimeTo(LocalTime timeTo) {
            this.timeTo = timeTo;
            return this;
        }

        public ShowSearchParametersDTOBuilder setPriceInEuroFrom(Integer priceInEuroFrom) {
            this.priceInEuroFrom = priceInEuroFrom;
            return this;
        }

        public ShowSearchParametersDTOBuilder setPriceInEuroTo(Integer priceInEuroTo) {
            this.priceInEuroTo = priceInEuroTo;
            return this;
        }

        public ShowSearchParametersDTOBuilder setEventName(String eventName) {
            this.eventName = eventName;
            return this;
        }

        public ShowSearchParametersDTOBuilder setHallName(String hallName) {
            this.hallName = hallName;
            return this;
        }
        public ShowSearchParametersDTOBuilder setDurationInMinutes (Integer durationInMinutes){
            this.durationInMinutes = durationInMinutes;
            return this;
        }

        public ShowSearchParametersDTO build() {
            return new ShowSearchParametersDTO(dateFrom, dateTo, timeFrom, timeTo, priceInEuroFrom, priceInEuroTo, eventName, hallName, durationInMinutes);
        }
    }
}

