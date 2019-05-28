package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.searchParameters;

import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class ShowSearchParametersDTO {
    @ApiModelProperty(name = "the id of the corresponding event")
    private Long eventId;
    @ApiModelProperty(name = "the minimum show date")
    private LocalDate dateFrom;
    @ApiModelProperty(name = "maximum show date")
    private LocalDate dateTo;
    @ApiModelProperty(name = "maximum show starting time")
    private LocalTime timeFrom;
    @ApiModelProperty(name = "minimum show starting time")
    private LocalTime timeTo;
    @ApiModelProperty(name = "the minimum price")
    private Integer priceInEuroFrom;
    @ApiModelProperty(name = "the maximum price")
    private Integer priceInEuroTo;
    @ApiModelProperty(name = "the name of the location")
    private String locationName;
    @ApiModelProperty(name = "the name of the event")
    private String eventName;
    @ApiModelProperty(name = "the name of the hall")
    private String hallName;
    @ApiModelProperty(name = "the duration of the Event")
    private Integer durationInMinutes;

    @ApiModelProperty(name = "The Country of the Location")
    private String country;
    @ApiModelProperty(name = "The city of the location")
    private String city;
    @ApiModelProperty(name = "the postal Code of the location")
    private String postalCode;
    @ApiModelProperty(name = "the street of the Location")
    private String street;
    @ApiModelProperty(name = "the house number of the Location")
    private Integer houseNr;


    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
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

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
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

    public Integer getHouseNr() {
        return houseNr;
    }

    public void setHouseNr(Integer houseNr) {
        this.houseNr = houseNr;
    }

    public void setDurationInMinutes(Integer durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public static class builder {
        private Long eventId;
        private LocalDate dateFrom;
        private LocalDate dateTo;
        private LocalTime timeFrom;
        private LocalTime timeTo;
        private Integer priceInEuroFrom;
        private Integer priceInEuroTo;
        private String eventName;
        private String locationName;
        private String hallName;
        private Integer durationInMinutes;
        private String country;
        private String city;
        private String postalCode;
        private String street;
        private Integer houseNr;

        public builder(){}

        public builder dateFrom(LocalDate dateFrom) {
            this.dateFrom = dateFrom;
            return this;
        }

        public builder eventId(Long eventId){
            this.eventId = eventId;
            return this;
        }

        public builder dateTo(LocalDate dateTo) {
            this.dateTo = dateTo;
            return this;
        }

        public builder timeFrom(LocalTime timeFrom) {
            this.timeFrom = timeFrom;
            return this;
        }

        public builder timeTo(LocalTime timeTo) {
            this.timeTo = timeTo;
            return this;
        }

        public builder priceInEuroFrom(Integer priceInEuroFrom) {
            this.priceInEuroFrom = priceInEuroFrom;
            return this;
        }

        public builder priceInEuroTo(Integer priceInEuroTo) {
            this.priceInEuroTo = priceInEuroTo;
            return this;
        }

        public builder eventName(String eventName) {
            this.eventName = eventName;
            return this;
        }

        public builder locationName(String locationName){
            this.locationName = locationName;
            return this;
        }

        public builder hallName(String hallName) {
            this.hallName = hallName;
            return this;
        }
        public builder durationInMinutes(Integer durationInMinutes){
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

        public builder houseNr(Integer houseNr){
            this.houseNr = houseNr;
            return this;
        }

        public ShowSearchParametersDTO build() {
            ShowSearchParametersDTO parametersDTO = new ShowSearchParametersDTO();
            parametersDTO.setEventId(eventId);
            parametersDTO.setDateFrom(dateFrom);
            parametersDTO.setDateTo(dateTo);
            parametersDTO.setDurationInMinutes(durationInMinutes);
            parametersDTO.setEventName(eventName);
            parametersDTO.setTimeTo(timeTo);
            parametersDTO.setTimeFrom(timeFrom);
            parametersDTO.setCountry(country);
            parametersDTO.setStreet(street);
            parametersDTO.setHouseNr(houseNr);
            parametersDTO.setCity(city);
            parametersDTO.setPostalCode(postalCode);
            parametersDTO.setHallName(hallName);
            parametersDTO.setPriceInEuroFrom(priceInEuroFrom);
            parametersDTO.setPriceInEuroTo(priceInEuroTo);
            parametersDTO.setLocationName(locationName);
            return parametersDTO;
        }

    }

    @Override
    public String toString() {
        return "ShowSearchParametersDTO{" +
            (eventId != null ? "eventId = " + eventId : "") +
            (dateFrom != null ? "dateFrom = " + dateFrom : "") +
            (dateTo != null ? " dateTo = " + dateTo : "") +
            (timeFrom != null ? " timeFrom = " + timeFrom : "") +
            (timeTo != null ? " timeTo = " + timeTo : "") +
            (priceInEuroFrom != null ? " priceInEuroFrom = " + priceInEuroFrom : "") +
            (priceInEuroTo != null ? " priceInEuroTo = " + priceInEuroTo : "") +
            (eventName != null ? " eventName = " + eventName : "") +
            (locationName != null ? " locationName = " + locationName : "") +
            (hallName != null ? " hallName = " + hallName  : "")  +
            (durationInMinutes != null ? " durationInMinutes = " + durationInMinutes : "") +
            (country != null ? " country = " + country : "") +
            (city != null ? "city = " + city : "") +
            (postalCode != null ? "postalcode = " + postalCode : "") +
            (street != null ? "street = " + street : "") +
            (houseNr != null ? "houseNumber = " + houseNr : "") +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShowSearchParametersDTO that = (ShowSearchParametersDTO) o;
        return Objects.equals(eventId, that.eventId) &&
            Objects.equals(dateFrom, that.dateFrom) &&
            Objects.equals(dateTo, that.dateTo) &&
            Objects.equals(timeFrom, that.timeFrom) &&
            Objects.equals(timeTo, that.timeTo) &&
            Objects.equals(priceInEuroFrom, that.priceInEuroFrom) &&
            Objects.equals(priceInEuroTo, that.priceInEuroTo) &&
            Objects.equals(locationName, that.locationName) &&
            Objects.equals(eventName, that.eventName) &&
            Objects.equals(hallName, that.hallName) &&
            Objects.equals(durationInMinutes, that.durationInMinutes) &&
            Objects.equals(country, that.country) &&
            Objects.equals(city, that.city) &&
            Objects.equals(postalCode, that.postalCode) &&
            Objects.equals(street, that.street) &&
            Objects.equals(houseNr, that.houseNr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, dateFrom, dateTo, timeFrom, timeTo, priceInEuroFrom, priceInEuroTo, locationName, eventName, hallName, durationInMinutes, country, city, postalCode, street, houseNr);
    }
}

