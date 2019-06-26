package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.searchParameters;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.location.LocationDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

public class HallSearchParametersDTO {

    @ApiModelProperty(name = "name of or substring of the name of a hall")
    private String name;
    @ApiModelProperty(name = "location of the hall for filtering halls by their location")
    private LocationDTO location;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocationDTO getLocation() {
        return location;
    }

    public void setLocation(LocationDTO location) {
        this.location = location;
    }

    public static HallSearchParametersDTOBuilder builder(){
        return new HallSearchParametersDTOBuilder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HallSearchParametersDTO)) return false;
        HallSearchParametersDTO that = (HallSearchParametersDTO) o;
        return Objects.equals(name, that.name) &&
            Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, location);
    }

    @Override
    public String toString() {
        return "HallSearchParametersDTO{" +
            "name='" + name + '\'' +
            ", location=" + location +
            '}';
    }

    public static class HallSearchParametersDTOBuilder {
        private String name;
        private LocationDTO location;

        public HallSearchParametersDTOBuilder name(String name){
            this.name = name;
            return this;
        }

        public HallSearchParametersDTOBuilder location(LocationDTO location){
            this.location = location;
            return this;
        }

        public HallSearchParametersDTO build(){
            HallSearchParametersDTO searchParameters = new HallSearchParametersDTO();
            searchParameters.setName(name);
            searchParameters.setLocation(location);
            return searchParameters;
        }
    }
}
