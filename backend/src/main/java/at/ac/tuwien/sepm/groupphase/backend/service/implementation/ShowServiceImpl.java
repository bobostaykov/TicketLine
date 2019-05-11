package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShowRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ShowService;

import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShowServiceImpl implements ShowService {

    private final ShowRepository showRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(ShowServiceImpl.class);

    ShowServiceImpl(ShowRepository showRepository) {
        this.showRepository = showRepository;
    }

    @Override
    public List<Show> findAllShowsFilteredByEventName(String eventName) {
        LOGGER.info("Find all shows filtered by event id");
        try {
            return showRepository.findAllShowsFilteredByEventName(eventName);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    /*
    @Override
    public List<Show> findAllShowsFilteredByLocationID(Integer locationID) {
        LOGGER.info("Find all shows filtered by location id");
        try {
            if (locationID < 0) throw new IllegalArgumentException("The location id is negative");
            return showRepository.findAllByLocationID(locationID);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public List<Show> findAllShows() {
        LOGGER.info("Find all shows");
        try{
            return showRepository.findAll();
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public List<Show> findAllShowsFiltered(LocalDateTime dateFrom, LocalDateTime dateTo, LocalDateTime timeFrom, LocalDateTime timeTo, Integer priceInEuroFrom, Integer priceInEuroTo, String eventName, String hallName) {
        LOGGER.info("Find all shows filtered by date, time, price, event or hall");
        return null;
    }

    @Override
    public List<Show> findAllShowsFilteredByLocation(String country, String city, String postalcode, String street) {
        LOGGER.info("Find all shows filtered by location");
        return null;
    }
     */
}
