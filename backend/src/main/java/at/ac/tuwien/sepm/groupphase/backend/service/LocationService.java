package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.location.LocationDTO;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface LocationService {

    /**
     * Finds locations filtered by the given attributes
     *
     * @param name of the location
     * @param country of the location
     * @param city of the location
     * @param street of the location
     * @param postalCode of the location
     * @param description of the location
     * @param page the number of the particular page to return
     * @return a page of the found locations
     * @throws ServiceException if something goes wrong
     */
    Page<LocationDTO> findLocationsFiltered(String name, String country, String city, String street, String postalCode, String description, Integer page) throws ServiceException;

    /**
     * Finds all locations filtered by the given attributes
     *
     * @param page the number of the particular page to return
     * @return a page of all locations that fit in it
     */
    Page<LocationDTO>  findAll(Integer page);

    /**
     * @return a list of all countries in the data base
     */
    List<String> getCountriesOrderedByName() throws ServiceException;
}
