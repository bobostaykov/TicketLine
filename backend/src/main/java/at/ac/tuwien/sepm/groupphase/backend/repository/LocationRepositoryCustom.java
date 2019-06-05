package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;

import java.util.List;

public interface LocationRepositoryCustom {

    /**
     *
     * @param country country of the location
     * @param city city of the location
     * @param street street of the location
     * @param postalCode postalcode of the location
     * @param description description of the location
     * @return
     */
    List<Location> findLocationsFiltered(String country, String city, String street, String postalCode, String description);

    /**
     * @return a list of the names of all countries ordered alphabetically
     */
    List<String> getCountriesOrderedByName() throws NotFoundException;
}
