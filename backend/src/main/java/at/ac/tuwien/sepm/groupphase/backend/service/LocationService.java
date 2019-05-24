package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.location.LocationDTO;

import java.util.List;

public interface LocationService {

    /**
     * gets a list of all locations saved in the backend
     * @return a list of all locations
     */
    List<LocationDTO>  findAllLocations();
}
