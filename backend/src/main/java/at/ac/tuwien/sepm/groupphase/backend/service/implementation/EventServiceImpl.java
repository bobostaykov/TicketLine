package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.datatype.EventType;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.event.EventDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.event.EventTicketsDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.searchParameters.EventSearchParametersDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.artist.ArtistMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.event.EventMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import javax.persistence.Tuple;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class EventServiceImpl implements EventService {

    private final ArtistRepository artistRepository;
    private final ArtistMapper artistMapper;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());


    public EventServiceImpl(ArtistMapper artistMapper, ArtistRepository artistRepository, EventRepository eventRepository, EventMapper eventMapper) {
        this.artistMapper = artistMapper;
        this.artistRepository = artistRepository;
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
    }

    @Override
    public EventDTO createEvent(EventDTO eventDTO) throws ServiceException {
        LOGGER.info("Event Service: createEvent");
        try {
            List<Artist> artistList = artistRepository.findAllByName(eventDTO.getArtist().getName());
            Artist artist;
            if (artistList.isEmpty()) {
                LOGGER.info("Event Service: create artist for event");
                artist = artistRepository.save(artistMapper.artistDTOToArtist(eventDTO.getArtist()));
            }else{
                artist = artistList.get(0);
            }
            eventDTO.setArtist(artistMapper.artistToArtistDTO(artist));
            return eventMapper.eventToEventDTO(eventRepository.save(eventMapper.eventDTOToEvent(eventDTO)));
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<EventTicketsDTO> findTopTenEvents(Set<String> monthsSet, Set<EventType> categoriesSet) throws ServiceException {
        LOGGER.info("Event Service: findTopTenEvents");
        ArrayList<EventTicketsDTO> toReturn = new ArrayList<>();
        try {
            for (Tuple pair: eventRepository.findTopTenEvents(monthsSet, categoriesSet)) {
                toReturn.add(new EventTicketsDTO((String)pair.get(0), (Long)pair.get(1)));
            }
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
        return toReturn;
    }

    @Override
    public Page<EventDTO> findAll(Integer page, Integer pageSize) throws ServiceException {
        LOGGER.info("Find all events");
        try {
            if(pageSize == null){
                pageSize = 10;
            }
            if(page < 0) {
                throw new IllegalArgumentException("Not a valid page.");
            }

            Pageable pageable;
            if(pageSize == -1){
                LOGGER.info("Find all events without pagination");
                pageable = Pageable.unpaged();
            } else {
                pageable = PageRequest.of(page, pageSize);
            }
            return eventRepository.findAllByOrderByNameAsc(pageable).map(eventMapper::eventToEventDTO);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Page<EventDTO> findAllFiltered(EventSearchParametersDTO parameters, Integer page, Integer pageSize) throws ServiceException{
        LOGGER.info("Find all events filtered by some attributes: " + parameters.toString());
        try{
            if(pageSize == null){
                pageSize = 10;
            }
            if(page < 0) {
                throw new IllegalArgumentException("Not a valid page.");
            }
            Pageable pageable = PageRequest.of(page, pageSize);
            Page<Event> page1 = eventRepository.findAllEventsFiltered(parameters, pageable);
            LOGGER.debug("Event Service: " + page1.getContent().toString());
            return page1.map(eventMapper::eventToEventDTO);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Page<EventDTO> findEventsFilteredByArtistID(Long id, Integer page, Integer pageSize) throws ServiceException{
        LOGGER.info("Event Service: findEventsFilteredByArtistID");
        try{
            if(pageSize == null){
                pageSize = 10;
            }
            if(page < 0) {
                throw new IllegalArgumentException("Not a valid page.");
            }
            Pageable pageable = PageRequest.of(page, pageSize);
            return eventRepository.findAllByArtist_Id(id, pageable).map(eventMapper::eventToEventDTO);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void deleteEvent(Long id) throws ServiceException {
        LOGGER.info("Remove event with id " + id);
        try {
            eventRepository.deleteById(id);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public EventDTO updateEvent(EventDTO eventDTO) {
        LOGGER.info("Update event: " + eventDTO.toString());
        Artist artist = artistRepository.findByName(eventDTO.getArtist().getName());
        eventDTO.setArtist(artistMapper.artistToArtistDTO(artist));
        return eventMapper.eventToEventDTO(eventRepository.save(eventMapper.eventDTOToEvent(eventDTO)));
    }

}
