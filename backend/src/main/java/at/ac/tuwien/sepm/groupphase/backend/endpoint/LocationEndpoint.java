package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.location.LocationDTO;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.LocationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(value = "/locations")
@Api(value = "locations")
public class LocationEndpoint {

    private LocationService locationService;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    LocationEndpoint(LocationService locationService) {
        this.locationService = locationService;
    }

    /*@RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Get all saved halls", authorizations = {@Authorization(value = "apiKey")})
    public List<LocationDTO> getLocations(){
        LOGGER.info("Getting all locations by name descending");
        return locationService.findAll();
    }*/

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Get all shows filtered by location", authorizations = {@Authorization(value = "apiKey")})
    public Page<LocationDTO> findLocationsFiltered(
        @RequestParam(value = "name", required = false) String name,
        @RequestParam(value = "country", required = false) String country,
        @RequestParam(value = "city", required = false) String city,
        @RequestParam(value = "street", required = false) String street,
        @RequestParam(value = "postalCode", required = false) String postalCode,
        @RequestParam(value = "description", required = false) String description,
        @RequestParam(value = "page", required = false) Integer page,
        @RequestParam(value = "pageSize", required = false) Integer pageSize
    ) {
        try {
            if (page != null && name == null && country == null && city == null && postalCode == null && street == null && description == null) {
                LOGGER.info("Location Endpoint: Get all locations");
                return locationService.findAll(page);
            } else {
                LOGGER.info("Location Endpoint: Get all locations filtered by some parameters");
                return locationService.findLocationsFiltered(name, country, city, street, postalCode, description, page, pageSize);
            }
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while looking for locations with those parameters: " + e.getMessage(), e);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while looking for locations with those parameters", e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No locations are found for the given parameters:" + e.getMessage(), e);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/countries")
    @ApiOperation(value = "Get all countries filtered alphabetically", authorizations = {@Authorization(value = "apiKey")})
    public List<String> getCountriesOrderedByName() {
        try {
            LOGGER.info("Location Endpoint: Get all countries ordered alphabetically");
            return locationService.getCountriesOrderedByName();
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while trying to get all countries ordered: ", e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No locations are found: " + e.getMessage(), e);
        }
    }
}
