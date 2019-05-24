package at.ac.tuwien.sepm.groupphase.backend.entity.mapper.location;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.location.LocationDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    Location locationDTOToLocation(LocationDTO locationDTO);

    LocationDTO locationToLocationDTO(Location location);

    List<LocationDTO> locationListToLocationDTOs(List<Location> locations);
}
