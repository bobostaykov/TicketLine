package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.hall;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.location.LocationDTO;

import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

//TODO add missing attributes
public class HallDTO {

    @ApiModelProperty(name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(name = "The name of the hall")
    private String name;

    @ApiModelProperty(name = "The location of the hall")
    private LocationDTO location;

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocationDTO getLocation() {
        return location;
    }

    public void setLocation(LocationDTO location) {
        this.location = location;
    }

    public static HallDTOBuilder builder() {
        return new HallDTOBuilder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HallDTO hallDTO = (HallDTO) o;
        return id.equals(hallDTO.id) &&
            name.equals(hallDTO.name) &&
            location.equals(hallDTO.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, location);
    }

    @Override
    public String toString() {
        return "HallDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", location=" + location +
            '}';
    }

    public static final class HallDTOBuilder{

        private Long id;
        private String name;
        private LocationDTO location;

        private HallDTOBuilder(){}

        public HallDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public HallDTOBuilder name(String name) {
            this.name = name;
            return this;
        }

        public HallDTOBuilder location(LocationDTO location) {
            this.location = location;
            return this;
        }

        public HallDTO build() {
            HallDTO hall = new HallDTO();
            hall.setId(id);
            hall.setName(name);
            hall.setLocation(location);
            return hall;
        }
    }
}
