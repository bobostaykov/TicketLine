package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.location;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

@ApiModel(value = "LocationDTO")
public class LocationDTO {

    @ApiModelProperty(name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(name = "Location's country")
    private String country;

    @ApiModelProperty(name = "Location's city")
    private String city;

    @ApiModelProperty(name = "Location's postal code")
    private String postalCode;

    @ApiModelProperty(name = "Location's street")
    private String street;

    @ApiModelProperty(name = "Location's description")
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        return Objects.equals(getId(), that.getId()) &&
            Objects.equals(getCountry(), that.getCountry()) &&
            Objects.equals(getCity(), that.getCity()) &&
            Objects.equals(getPostalCode(), that.getPostalCode()) &&
            Objects.equals(getStreet(), that.getStreet()) &&
            Objects.equals(getDescription(), that.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCountry(), getCity(), getPostalCode(), getStreet(), getDescription());
    }
    @Override
    public String toString() {
        return "LocationDTO{" +
            "id=" + id +
            ", country='" + country + '\'' +
            ", city='" + city + '\'' +
            ", postalCode='" + postalCode + '\'' +
            ", street='" + street + '\'' +
            ", description='" + description + '\'' +
            '}';
    }

    public static final class LocationDTOBuilder {
        private Long id;
        private String country;
        private String city;
        private String postalCode;
        private String street;
        private String description;

        private LocationDTOBuilder() {}

        public LocationDTOBuilder id(Long id) {
            this.id = id;
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

        public LocationDTOBuilder description(String description) {
            this.description = description;
            return this;
        }

        public LocationDTO build() {
            LocationDTO locationDTO = new LocationDTO();
            locationDTO.setId(id);
            locationDTO.setCountry(country);
            locationDTO.setCity(city);
            locationDTO.setPostalCode(postalCode);
            locationDTO.setStreet(street);
            locationDTO.setDescription(description);
            return locationDTO;
        }
    }
}
