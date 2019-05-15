package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.hall;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.location.LocationDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.show.ShowDTO;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

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
}
