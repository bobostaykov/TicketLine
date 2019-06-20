package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.searchParameters.ShowSearchParametersDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.show.ShowDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.show.ShowMapper;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShowRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ShowService;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;

//TODO Class is unfinished
@Service
public class ShowServiceImpl implements ShowService {


    @Autowired
    private ShowRepository showRepository;
    @Autowired
    private  ShowMapper showMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(ShowServiceImpl.class);

    public ShowServiceImpl(ShowRepository showRepository, ShowMapper showMapper) {
        this.showRepository = showRepository;
        this.showMapper = showMapper;
    }
    public ShowServiceImpl(){}

    /*
    @Override
    public Page<ShowDTO> findAllShowsFilteredByEventName(String eventName, Integer page) {
        LOGGER.info("Find all shows filtered by event id");
        try {
            int pageSize = 10;
            if(page < 0) {
                throw new IllegalArgumentException("Not a valid page.");
            }
            Pageable pageable = PageRequest.of(page, pageSize);
            Page<Show> page1 = showRepository.findAllShowsFilteredByEventName(eventName, pageable);
            LOGGER.debug("PageToString: " + page1.toString());
            LOGGER.debug("TotalElements: " + page1.getTotalElements());
            LOGGER.debug("TotalPages: " + page1.getTotalPages());
            LOGGER.debug("Content: " + page1.getContent());
            LOGGER.debug("Size: " + page1.getContent().size());
            return page1.map(showMapper::showToShowDTO);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }
    */

    @Override
    public Page<ShowDTO> findAll(Integer page) throws ServiceException {
        LOGGER.info("Show Service: Find all shows");
        try {
            int pageSize = 10;
            if(page < 0) {
                throw new IllegalArgumentException("Not a valid page.");
            }
            Pageable pageable = PageRequest.of(page, pageSize);
            return showRepository.findAll(pageable).map(showMapper::showToShowDTO);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Page<ShowDTO> findAllShowsFiltered(ShowSearchParametersDTO parameters, Integer page) throws ServiceException {
        try{
            LOGGER.info("Show Service: Find all shows filtered by :" + parameters.toString());
            if(page < 0) {
                throw new IllegalArgumentException("Not a valid page.");
            }
            return showRepository.findAllShowsFiltered(parameters, page).map(showMapper::showToShowDTO);
        }catch (PersistenceException e){
            throw new ServiceException(e.getMessage(), e);
        }

    }

    @Override
    public Page<ShowDTO> findAllShowsFilteredByLocationID(Long locationID, Integer page) {
        LOGGER.info("Show Service: Find all shows filtered by location id");
        try {
            if (locationID < 0) throw new IllegalArgumentException("The location id is negative");
            int pageSize = 10;
            if (page < 0) {
                throw new IllegalArgumentException("Not a valid page.");
            }
            Pageable pageable = PageRequest.of(page, pageSize);
            return showRepository.findAllByHall_Location_Id(locationID, pageable).map(showMapper::showToShowDTO);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteById(Long showId) throws ServiceException, DataIntegrityViolationException {
        LOGGER.info("ShowService: deleteById " + showId);
        try {
            showRepository.deleteById(showId);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /*
    private static Predicate<ShowDTO> compareMinPrice(Double minPrice){
        return show -> show.getPricePattern()
            .getPriceMapping()
            .values()
            .stream()
            .min(Comparator
                .comparingDouble(Double :: doubleValue))
            .get() > minPrice;
    }

    private static Predicate<ShowDTO> compareMaxPrice(Double maxPrice){
        return show -> show.getPricePattern()
            .getPriceMapping()
            .values()
            .stream()
            .min(Comparator
                .comparingDouble(Double::doubleValue))
            .get() > maxPrice;
    }
     */
}
