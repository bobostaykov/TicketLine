package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.requestparameter.ShowRequestParameter;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.searchParameters.ShowSearchParametersDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.show.ShowDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.show.ShowMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShowRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ShowService;
import at.ac.tuwien.sepm.groupphase.backend.service.ticketExpirationHandler.TicketExpirationHandler;
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
import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

import javax.validation.constraints.Positive;

//TODO Class is unfinished
@Service
public class ShowServiceImpl implements ShowService {


    @Autowired
    private ShowRepository showRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private ShowMapper showMapper;
    private final TicketExpirationHandler ticketExpirationHandler;
    private static final Logger LOGGER = LoggerFactory.getLogger(ShowServiceImpl.class);

    public ShowServiceImpl(ShowRepository showRepository, ShowMapper showMapper, TicketRepository ticketRepository,
                           TicketExpirationHandler ticketExpirationHandler) {
        this.showRepository = showRepository;
        this.ticketRepository = ticketRepository;
        this.showMapper = showMapper;
        this.ticketExpirationHandler = ticketExpirationHandler;
    }

    //public ShowServiceImpl(){}
    @Override
    public Page<ShowDTO> findAll(Integer page) throws ServiceException {
        LOGGER.info("Show Service: Find all shows");
        try {
            int pageSize = 10;
            if (page < 0) {
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
        try {
            LOGGER.info("Show Service: Find all shows filtered by :" + parameters.toString());
            if (pageSize == null) {
                pageSize = 10;
            }
            if (page < 0) {
                throw new IllegalArgumentException("Not a valid page.");
            }
            Pageable pageable = PageRequest.of(page, pageSize);
            return showRepository.findAllShowsFiltered(parameters, pageable).map(showMapper::showToShowDTO);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }

    }

    @Override
    public Page<ShowDTO> findAllShowsFilteredByLocationID(Long locationID, Integer page, Integer pageSize) {
        LOGGER.info("Show Service: Find all shows filtered by location id");
        try {
            if (locationID < 0) throw new IllegalArgumentException("The location id is negative");
            if (pageSize == null) {
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
    public List<ShowDTO> findSearchResultSuggestions(String eventName, String date, String time) {
        LOGGER.info("Show Service: Find shows matching eventName = " + eventName + ", date = " + date + ", time = " + time);
        return showMapper.showToShowDTO(showRepository.findByEventNameAndShowDateAndShowTime(eventName, date, time));
    }

    @Override
    public ShowDTO findOneById(Long id, List<ShowRequestParameter> include) {
        Show show = showRepository.findOneById(id).orElseThrow(NotFoundException::new);
        ShowDTO showDTO = showMapper.showToShowDTO(show);
        if (include != null && include.contains(ShowRequestParameter.TICKETS)) {
            ticketExpirationHandler.setExpiredReservatedTicketsToStatusExpiredForSpecificShow(showDTO);
            List<Ticket> tickets = ticketRepository.findAllByShowId(showDTO.getId());
            if (!isEmpty(showDTO.getHall().getSeats())) {
                for (Ticket ticket : tickets) {
                    // set ticket status for ever seat associated with the show that has already been sold or reserved
                    Seat seat = ticket.getSeat();
                    if (seat != null) {
                        showDTO.getHall().getSeats().stream()
                            .filter(seatDTO -> seatDTO.getId().equals(seat.getId()))
                            .findFirst().ifPresent(seatDTO -> seatDTO.setTicketStatus(ticket.getStatus()));
                    }
                }
            } else if (!isEmpty(showDTO.getHall().getSectors())) {
                for (Ticket ticket : tickets) {
                    Sector sector = ticket.getSector();
                    if (sector != null) {
                        showDTO.getHall().getSectors().stream()
                            .filter(sectorDTO -> sectorDTO.getId().equals(sector.getId()))
                            .findFirst().ifPresent(sectorDTO -> sectorDTO.setTicketStatus(ticket.getStatus()));
                    }
                }
            }
        }
        return showDTO;
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
