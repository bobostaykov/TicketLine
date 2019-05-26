package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.location.LocationDTO;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;

import java.util.List;

public interface LocationService {

    /**
     * Finds locations filtered by the given attributes
     *
     * @param country of the location
     * @param city of the location
     * @param street of the location
     * @param postalCode of the location
     * @param description of the location
     * @return a list of LocationDTOs
     */
    List<LocationDTO> findLocationsFiltered(String country, String city, String street, String postalCode, String description) throws ServiceException;

    /** gets a list of all locations saved in the backend
     * @return a list of all locations
     */
    List<LocationDTO>  findAll();
}
