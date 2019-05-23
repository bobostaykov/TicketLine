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

    public ShowSearchParametersDTO(LocalDate dateFrom, LocalDate dateTo, LocalTime timeFrom, LocalTime timeTo, Integer priceInEuroFrom, Integer priceInEuroTo, String eventName, String hallName,Integer durationInMinutes) {
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

    public builder builder(){return new builder();}

    public Integer getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(Integer durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public static class builder {
        private LocalDate dateFrom;
        private LocalDate dateTo;
        private LocalTime timeFrom;
        private LocalTime timeTo;
        private Integer priceInEuroFrom;
        private Integer priceInEuroTo;
        private String eventName;
        private String hallName;
        private Integer durationInMinutes;

        public builder(){}

        public builder setDateFrom(LocalDate dateFrom) {
            this.dateFrom = dateFrom;
            return this;
        }

        public builder setDateTo(LocalDate dateTo) {
            this.dateTo = dateTo;
            return this;
        }

        public builder setTimeFrom(LocalTime timeFrom) {
            this.timeFrom = timeFrom;
            return this;
        }

        public builder setTimeTo(LocalTime timeTo) {
            this.timeTo = timeTo;
            return this;
        }

        public builder setPriceInEuroFrom(Integer priceInEuroFrom) {
            this.priceInEuroFrom = priceInEuroFrom;
            return this;
        }

        public builder setPriceInEuroTo(Integer priceInEuroTo) {
            this.priceInEuroTo = priceInEuroTo;
            return this;
        }

        public builder setEventName(String eventName) {
            this.eventName = eventName;
            return this;
        }

        public builder setHallName(String hallName) {
            this.hallName = hallName;
            return this;
        }
        public builder setDurationInMinutes(Integer durationInMinutes){
            this.durationInMinutes = durationInMinutes;
            return this;
        }

        public ShowSearchParametersDTO build() {
            return new ShowSearchParametersDTO(dateFrom, dateTo, timeFrom, timeTo, priceInEuroFrom, priceInEuroTo, eventName, hallName, durationInMinutes);
        }

    }

    @Override
    public String toString() {
        return "ShowSearchParametersDTO{" +
            (dateFrom != null ? "dateFrom=" + dateFrom : "") +
            (dateTo != null ? " dateTo=" + dateTo : "") +
            (timeFrom != null ? " timeFrom=" + timeFrom : "") +
            (timeTo != null ? " timeTo=" + timeTo : "") +
            (priceInEuroFrom != null ? " priceInEuroFrom=" + priceInEuroFrom : "") +
            (priceInEuroTo != null ? " priceInEuroTo=" + priceInEuroTo : "") +
            (eventName != null ? " eventName='" + eventName + '\'': "") +
            (hallName != null ? " hallName='" + hallName + '\'' : "")  +
            (durationInMinutes != null ? " durationInMinutes=" + durationInMinutes : "") +
            '}';
    }
}

