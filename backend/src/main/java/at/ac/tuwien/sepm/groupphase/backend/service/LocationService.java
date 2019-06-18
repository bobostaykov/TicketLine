package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.location.LocationDTO;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.repository.projections.SimpleLocation;
import org.springframework.data.domain.Page;

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
     * @param page the number of the particular page to return
     * @return a page of the found locations
     * @throws ServiceException
     */
    Page<LocationDTO> findLocationsFiltered(String country, String city, String street, String postalCode, String description, Integer page) throws ServiceException;

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

    /**
     * returns a list of locations as location ids and names that match parameter name
     * @param name is a substring of the names of all locations that will be returned
     * @return a list of locations with only id and name containing param name as a substring
     */
    List<SimpleLocation> findSearchResultSuggestions(String name);

    /**
     * finds and returns one location by its id
     * @param id of location to be found
     * @return location dto with it matching param
     */
    LocationDTO findOneById(Long id);
}
