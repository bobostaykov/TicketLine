package at.ac.tuwien.sepm.groupphase.backend.entity.mapper.location;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.location.LocationDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    /**
     * Maps LocationDTO object to Location object
     * @param locationDTO to map
     * @return the mapped Location object
     */
    Location locationDTOToLocation(LocationDTO locationDTO);

    /**
     * Maps Location object to LocationDTO object
     * @param location to map
     * @return the mapped LocationDTO object
     */
    LocationDTO locationToLocationDTO(Location location);
}
