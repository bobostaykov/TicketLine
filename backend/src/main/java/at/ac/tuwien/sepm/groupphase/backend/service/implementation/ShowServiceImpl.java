package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.searchParameters.ShowSearchParametersDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.show.ShowDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.show.ShowMapper;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShowRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ShowService;

import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

//TODO Class is unfinished
@Service
public class ShowServiceImpl implements ShowService {

    private final ShowRepository showRepository;
    private final ShowMapper showMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(ShowServiceImpl.class);

    ShowServiceImpl(ShowRepository showRepository, ShowMapper showMapper) {
        this.showRepository = showRepository;
        this.showMapper = showMapper;
    }

    @Override
    public List<ShowDTO> findAllShows() throws at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException {
        return showMapper.showToShowDTO(showRepository.findAll());
    }

    @Override
    public List<ShowDTO> findAllShowsFilteredByEventName(String eventName) {
        LOGGER.info("Find all shows filtered by event id");
        try {
            return showMapper.showToShowDTO(showRepository.findAllShowsFilteredByEventName(eventName));
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public List<ShowDTO> findAll() {
        LOGGER.info("Show Service: Find all shows");
        try{
            return showMapper.showToShowDTO(showRepository.findAll());
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
*/
    @Override
    public List<ShowDTO> findAllShowsFiltered(ShowSearchParametersDTO parameters) throws ServiceException {
        try{
            LOGGER.info("Find all shows filtered by :" + parameters.toString());
            return showMapper.showToShowDTO(showRepository.findAllShowsFiltered(parameters));

        }catch (PersistenceException e){
            throw new ServiceException(e.getMessage(), e);
        }

    }


    @Override
    public List<ShowDTO> findAllShowsFilteredByLocation(String country, String city, String postalcode, String street) {
        LOGGER.info("Find all shows filtered by location");
        return null;
    }
    private static Predicate<Show> compareMin(Double minPrice){
        return show -> show.getPricePattern()
            .getPriceMapping()
            .values()
            .stream()
            .min(Comparator
                .comparingDouble(value -> value.doubleValue())).get().doubleValue() > minPrice;
    }

}
