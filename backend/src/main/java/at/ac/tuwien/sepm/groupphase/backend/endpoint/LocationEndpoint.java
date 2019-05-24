package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.location.LocationDTO;
import at.ac.tuwien.sepm.groupphase.backend.service.LocationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/locations")
@Api(value = "locations")
public class LocationEndpoint {

    private final LocationService locationService;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public LocationEndpoint(LocationService locationService){
        this.locationService = locationService;
    }

    @GetMapping
    @ApiOperation(value = "Get all saved halls", authorizations = {@Authorization(value = "apiKey")})
    public List<LocationDTO> getLocations(){
        LOGGER.info("Getting all locations");
        return locationService.findAllLocations();
    }
}
