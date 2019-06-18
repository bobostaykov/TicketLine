package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface LocationRepositoryCustom {

    /**
     * Find locations filtered by attributes
     *
     * @param name of the location
     * @param country of the location
     * @param city of the location
     * @param street of the location
     * @param postalCode of the location
     * @param description of the location
     * @param page the number of the page to return
     * @return a page of the found locations
     */
    Page<Location> findLocationsFiltered(String name, String country, String city, String street, String postalCode, String description, Integer page);

    /**
     * @return a list of the names of all countries ordered alphabetically
     */
    List<String> getCountriesOrderedByName() throws NotFoundException;
}
