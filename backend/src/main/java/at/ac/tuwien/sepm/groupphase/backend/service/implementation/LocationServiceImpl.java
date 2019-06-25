package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.location.LocationDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.location.LocationMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.projections.SimpleLocation;
import at.ac.tuwien.sepm.groupphase.backend.service.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {

    private LocationRepository locationRepository;
    private LocationMapper locationMapper;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    LocationServiceImpl(LocationRepository locationRepository, LocationMapper locationMapper){
        this.locationMapper = locationMapper;
        this.locationRepository = locationRepository;
    }

    @Override
    public Page<LocationDTO> findLocationsFiltered(String name, String country, String city, String street, String postalCode, String description, Integer page, Integer pageSize) throws ServiceException {
        LOGGER.info("Location Service: findLocationsFiltered()");
        try {
            if (name != null && name.equals("")) name = null;
            if (country != null && country.equals("")) country = null;
            if (city != null && city.equals("")) city = null;
            if (street != null && street.equals("")) street = null;
            if (postalCode != null && postalCode.equals("")) postalCode = null;
            if (description != null && description.equals("")) description = null;

            if(pageSize == null){
                pageSize = 10;
            }

            if(page < 0) {
                throw new IllegalArgumentException("Not a valid page.");
            }
            Pageable pageable = PageRequest.of(page, pageSize);
            return locationRepository.findLocationsFiltered(name, country, city, street, postalCode, description, pageable).map(locationMapper::locationToLocationDTO);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Page<LocationDTO> findAll(Integer page) {
        LOGGER.info("Location Service: Retrieving a list of all locations from repository");
        int pageSize = 10;
        if(page < 0) {
            throw new IllegalArgumentException("Not a valid page.");
        }
        Pageable pageable = PageRequest.of(page, pageSize);
        return locationRepository.findAll(pageable).map(locationMapper::locationToLocationDTO);
    }

    @Override
    public List<String> getCountriesOrderedByName() throws ServiceException{
        LOGGER.info("Location Service: Retrieving a list of all countries ordered alphabetically");
        try {
            return locationRepository.getCountriesOrderedByName();
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public List<SimpleLocation> findSearchResultSuggestions(String name) {
        LOGGER.info("Location Service: Retrieving a list of locations with only name and ID set by search parameter name ="
        + name);
        return locationRepository.findByLocationNameContainingIgnoreCase(name);
    }

    @Override
    public LocationDTO findOneById(Long id) {
        LOGGER.info("Location Service: Retrieving a location by id " + id);
        return locationMapper.locationToLocationDTO(locationRepository.findOneById(id).orElseThrow(NotFoundException::new));
    }

    @Override
    public void deleteById(Long locationId) throws ServiceException, DataIntegrityViolationException {
        LOGGER.info("LocationService: deleteById " + locationId);
        try {
            locationRepository.deleteById(locationId);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public LocationDTO updateLocation(LocationDTO locationDTO) {
        LOGGER.info("LocationService: Updating a location: " + locationDTO.toString());
        return locationMapper.locationToLocationDTO(locationRepository.save(locationMapper.locationDTOToLocation(locationDTO)));

    }

    @Override
    public LocationDTO addLocation(LocationDTO locationDTO) {
        LOGGER.info("LocationService: Adding a location: " + locationDTO.toString());
        return locationMapper.locationToLocationDTO(locationRepository.save(locationMapper.locationDTOToLocation(locationDTO)));
    }
}
