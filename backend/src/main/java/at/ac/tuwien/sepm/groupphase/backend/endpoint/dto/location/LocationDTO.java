package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.location;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Size;
import java.util.Objects;

@ApiModel(value = "LocationDTO")
public class LocationDTO {

    @ApiModelProperty(name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(name = "Locations's name")
    private String locationName;

    @ApiModelProperty(name = "Location's country")
    private String country;

    @ApiModelProperty(name = "Location's city")
    private String city;

    @ApiModelProperty(name = "Location's postal code")
    private String postalCode;

    @ApiModelProperty(name = "Location's street")
    private String street;

    @ApiModelProperty(name = "Locations houseNr")
    private Integer houseNr;

    @ApiModelProperty(name = "Location's description")
    private String description;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static LocationDTOBuilder builder() { return new LocationDTOBuilder(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationDTO that = (LocationDTO) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(locationName, that.locationName) &&
            Objects.equals(country, that.country) &&
            Objects.equals(city, that.city) &&
            Objects.equals(postalCode, that.postalCode) &&
            Objects.equals(street, that.street) &&
            Objects.equals(houseNr, that.houseNr) &&
            Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, locationName, country, city, postalCode, street, houseNr, description);
    }

    @Override
    public String toString() {
        return "LocationDTO{" +
            "id=" + id +
            ", locationName='" + locationName + '\'' +
            ", country='" + country + '\'' +
            ", city='" + city + '\'' +
            ", postalCode='" + postalCode + '\'' +
            ", street='" + street + '\'' +
            ", houseNr=" + houseNr +
            ", description='" + description + '\'' +
            '}';
    }

    public static final class LocationDTOBuilder {
        private Long id;
        private String locationName;
        private String country;
        private String city;
        private String postalCode;
        private String street;
        private Integer houseNr;
        private String description;


        private LocationDTOBuilder() {}

        public LocationDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public LocationDTOBuilder locationName(String locationName){
            this.locationName = locationName;
            return this;
        }

        public LocationDTOBuilder country(String country) {
            this.country = country;
            return this;
        }

        public LocationDTOBuilder city(String city) {
            this.city = city;
            return this;
        }

        public LocationDTOBuilder postalCode(String postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        public LocationDTOBuilder street(String street) {
            this.street = street;
            return this;
        }

        public LocationDTOBuilder houseNr(Integer houseNr){
            this.houseNr = houseNr;
            return this;
        }

        public LocationDTOBuilder description(String description) {
            this.description = description;
            return this;
        }

        public LocationDTO build() {
            LocationDTO locationDTO = new LocationDTO();
            locationDTO.setId(id);
            locationDTO.setLocationName(locationName);
            locationDTO.setCountry(country);
            locationDTO.setCity(city);
            locationDTO.setPostalCode(postalCode);
            locationDTO.setStreet(street);
            locationDTO.setHouseNr(houseNr);
            locationDTO.setDescription(description);
            return locationDTO;
        }
    }
}
