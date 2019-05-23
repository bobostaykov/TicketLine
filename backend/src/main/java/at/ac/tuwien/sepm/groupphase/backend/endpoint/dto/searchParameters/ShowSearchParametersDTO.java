package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.searchParameters;

import java.time.LocalDate;
import java.time.LocalTime;

public class ShowSearchParametersDTO {
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private LocalTime timeFron;
    private LocalTime timeTo;
    private Integer priceInEuroFrom;
    private Integer priceInEuroTo;
    private String eventName;
    private String hallName;

    public ShowSearchParametersDTO(LocalDate dateFrom, LocalDate dateTo, LocalTime timeFrom, LocalTime timeTo, Integer priceInEuroFrom, Integer priceInEuroTo, String eventName, String hallName) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.timeFron = timeFrom;
        this.timeTo = timeTo;
        this.priceInEuroFrom = priceInEuroFrom;
        this.priceInEuroTo = priceInEuroTo;
        this.eventName = eventName;
        this.hallName = hallName;
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
        return timeFron;
    }

    public void setTimeFron(LocalTime timeFron) {
        this.timeFron = timeFron;
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
}

