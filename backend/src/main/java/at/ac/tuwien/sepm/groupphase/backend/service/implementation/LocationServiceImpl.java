package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.location.LocationDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.location.LocationMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import javax.validation.constraints.Positive;
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
    public Page<LocationDTO> findLocationsFiltered(String name, String country, String city, String street, String postalCode, String description, Integer page, @Positive Integer pageSize) throws ServiceException {
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
}
