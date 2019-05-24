package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.location.LocationDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.location.LocationMapper;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public LocationServiceImpl(LocationRepository locationRepository, LocationMapper locationMapper){
        this.locationRepository = locationRepository;
        this.locationMapper = locationMapper;
    }

    @Override
    public List<LocationDTO> findAllLocations() {
        LOGGER.info("Retrieving a list of all locations from repository");
        return locationMapper.locationListToLocationDTOs(locationRepository.findAll());
    }
}
