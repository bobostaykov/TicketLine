package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.searchParameters;

import io.swagger.annotations.ApiModelProperty;

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

    @ApiModelProperty(name = "The Country of the Location")
    private String country;
    @ApiModelProperty(name = "The city of the location")
    private String city;
    @ApiModelProperty(name = "the postal Code of the location")
    private String postalCode;
    @ApiModelProperty(name = "the street of the Location")
    private String street;

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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
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
        private String country;
        private String city;
        private String postalCode;
        private String street;

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
        public builder country(String country){
            this.country = country;
            return this;
        }

        public builder city (String city){
            this.city = city;
            return this;
        }

        public builder postalcode (String postalCode){
            this.postalCode = postalCode;
            return this;
        }

        public builder street (String street){
            this.street = street;
            return this;
        }

        public ShowSearchParametersDTO build() {
            ShowSearchParametersDTO parametersDTO = new ShowSearchParametersDTO();
            parametersDTO.setDateFrom(dateFrom);
            parametersDTO.setDateTo(dateTo);
            parametersDTO.setDurationInMinutes(durationInMinutes);
            parametersDTO.setEventName(eventName);
            parametersDTO.setTimeTo(timeTo);
            parametersDTO.setTimeFrom(timeFrom);
            parametersDTO.setCountry(country);
            parametersDTO.setStreet(street);
            parametersDTO.setCity(city);
            parametersDTO.setPostalCode(postalCode);
            parametersDTO.setHallName(hallName);
            parametersDTO.setPriceInEuroFrom(priceInEuroFrom);
            parametersDTO.setPriceInEuroTo(priceInEuroTo);
            return parametersDTO;
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
            (country != null ? " country = " + country : "") +
            (city != null ? "city = " + city : "") +
            (postalCode != null ? "postalcode = " + postalCode : "") +
            (street != null ? "street = " + street : "") +
            '}';
    }
}

