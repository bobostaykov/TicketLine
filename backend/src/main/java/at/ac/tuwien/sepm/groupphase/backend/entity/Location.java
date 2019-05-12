package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

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

    @Column(nullable = false, name = "postalcode")
    @Size(max = 16)
    private String postalcode;

    @Column(nullable = false, name = "street")
    @Size(max = 64)
    private String street;

    @Column(name = "description")
    @Size(max = 128)
    private String description;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "location", orphanRemoval = true)
    private List<Hall> halls;

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

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
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

    public List<Hall> getHalls() {
        return halls;
    }

    public void setHalls(List<Hall> halls) {
        this.halls = halls;
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
            postalcode.equals(location.postalcode) &&
            street.equals(location.street) &&
            Objects.equals(description, location.description) &&
            Objects.equals(halls, location.halls);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, country, city, postalcode, street, description, halls);
    }

    @Override
    public String toString() {
        return "Location{" +
            "id=" + id +
            ", country='" + country + '\'' +
            ", city='" + city + '\'' +
            ", postalcode='" + postalcode + '\'' +
            ", street='" + street + '\'' +
            ", description='" + description + '\'' +
            ", halls=" + halls +
            '}';
    }

    public static final class LocationBuilder {
        private Long id;
        private String country;
        private String city;
        private String postalcode;
        private String street;
        private String description;
        private List<Hall> halls;

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

        public LocationBuilder hall(List<Hall> halls) {
            this.halls = halls;
            return this;
        }


        public Location build() {
            Location location = new Location();
            location.setId(id);
            location.setCountry(country);
            location.setCity(city);
            location.setPostalcode(postalcode);
            location.setStreet(street);
            location.setDescription(description);
            location.setHalls(halls);
            return location;
        }
    }
}
