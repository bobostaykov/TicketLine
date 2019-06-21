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
import javax.validation.constraints.Positive;

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
    public Page<ShowDTO> findAllShowsFiltered(ShowSearchParametersDTO parameters, Integer page, Integer pageSize) throws ServiceException {
        try{
            LOGGER.info("Show Service: Find all shows filtered by :" + parameters.toString());
            if(pageSize == null){
                pageSize = 10;
            }
            if(page < 0) {
                throw new IllegalArgumentException("Not a valid page.");
            }
            Pageable pageable = PageRequest.of(page, pageSize);
            return showRepository.findAllShowsFiltered(parameters, pageable).map(showMapper::showToShowDTO);
        }catch (PersistenceException e){
            throw new ServiceException(e.getMessage(), e);
        }

    }

    @Override
    public Page<ShowDTO> findAllShowsFilteredByLocationID(Long locationID, Integer page, Integer pageSize) {
        LOGGER.info("Show Service: Find all shows filtered by location id");
        try {
            if (locationID < 0) throw new IllegalArgumentException("The location id is negative");
            if(pageSize == null){
                pageSize = 10;
            }
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

}
