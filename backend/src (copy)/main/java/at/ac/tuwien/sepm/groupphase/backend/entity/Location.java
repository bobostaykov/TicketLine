package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Objects;

//TODO put nullable in some classes by the references
@Entity
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_location_id")
    @SequenceGenerator(name = "seq_location_id", sequenceName = "seq_location_id")
    private Long id;

    @Column(nullable = false, name = "country")
    @Size(max = 64)
    private String country;

    @Column(nullable = false, name = "city")
    @Size(max = 64)
    private String city;

    @Column(nullable = false, name = "postal_code")
    @Size(max = 16)
    private String postalCode;

    @Column(nullable = false, name = "street")
    @Size(max = 64)
    private String street;

    @Column(name = "description")
    @Size(max = 128)
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

    public static LocationBuilder builder() {
        return new LocationBuilder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return id.equals(location.id) &&
            country.equals(location.country) &&
            city.equals(location.city) &&
            postalCode.equals(location.postalCode) &&
            street.equals(location.street) &&
            Objects.equals(description, location.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, country, city, postalCode, street, description);
    }

    @Override
    public String toString() {
        return "Location{" +
            "id=" + id +
            ", country='" + country + '\'' +
            ", city='" + city + '\'' +
            ", postalCode='" + postalCode + '\'' +
            ", street='" + street + '\'' +
            ", description='" + description + '\'' +
            '}';
    }

    public static final class LocationBuilder {
        private Long id;
        private String country;
        private String city;
        private String postalcode;
        private String street;
        private String description;

        private LocationBuilder() {}

        public LocationBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public LocationBuilder country(String country) {
            this.country = country;
            return this;
        }

        public LocationBuilder city(String city) {
            this.city = city;
            return this;
        }

        public LocationBuilder postalcode(String postalcode) {
            this.postalcode = postalcode;
            return this;
        }

        public LocationBuilder street(String street) {
            this.street = street;
            return this;
        }

        public LocationBuilder description(String description) {
            this.description = description;
            return this;
        }

        public Location build() {
            Location location = new Location();
            location.setId(id);
            location.setCountry(country);
            location.setCity(city);
            location.setPostalCode(postalcode);
            location.setStreet(street);
            location.setDescription(description);
            return location;
        }
    }
}
