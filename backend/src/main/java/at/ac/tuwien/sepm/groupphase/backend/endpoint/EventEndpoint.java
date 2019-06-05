package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.datatype.EventType;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.event.EventDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.event.EventTicketsDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.searchParameters.EventSearchParametersDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.event.EventTicketsMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping(value = "/events")
@Api(value = "events")
public class EventEndpoint {

    private final EventService eventService;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final EventTicketsMapper eventTicketsMapper;

    public EventEndpoint(EventService eventService, EventTicketsMapper eventTicketsMapper) {
        this.eventService = eventService;
        this.eventTicketsMapper = eventTicketsMapper;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/topten")
    @ApiOperation(value = "Get top 10 events", authorizations = {@Authorization(value = "apiKey")})
    public List<EventTicketsDTO> findTopTenEvents(@RequestParam(value = "months") String months, @RequestParam(value = "categories") String categories) {
        LOGGER.info("Event Endpoint: findTopTenEvents");
        Set<String> monthsSet = new HashSet<>(Arrays.asList(months.split(",")));
        Set<EventType> categoriesSet = new HashSet<>();
        List<EventTicketsDTO> topTen = new ArrayList<>();

        for (String s: categories.split(",")) {
            categoriesSet.add(EventType.valueOf(s));
        }
        try {
            return new ArrayList<>(eventService.findTopTenEvents(monthsSet, categoriesSet));
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Get all events", authorizations = {@Authorization(value = "apiKey")})
    public List<EventDTO> findEventsFilteredByAttributes(@RequestParam(value = "eventName", required = false) String eventName,
                                                         @RequestParam(value = "eventType", required = false) String eventType,
                                                         @RequestParam(value = "content", required = false) String content,
                                                         @RequestParam(value = "description", required = false) String description,
                                                         @RequestParam(value = "duration", required = false) Integer duration,
                                                         @RequestParam(value = "artistName", required = false) String artistName) {
        if (eventName == null && eventType == null && content == null && description == null && duration == null && artistName == null) {
            LOGGER.info("Event Endpoint: findAll");
            try {
                return eventService.findAll();
            } catch (ServiceException e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
            }
        } else {
            LOGGER.info("Event Endpoint: findEventsFilteredByAttributes");
            EventType eventTypeConv = null;
            if (eventType != null) {
                for (EventType type : EventType.values()
                ) {
                    if (eventType.equalsIgnoreCase(type.name())) {
                        eventTypeConv = type;
                    }

                }
            }
            EventSearchParametersDTO parameters = EventSearchParametersDTO.builder()
                .setName(eventName)
                .setContent(content)
                .setDurationInMinutes(duration)
                .setArtistName(artistName)
                .setDescription(description)
                .setEventType(eventTypeConv)
                .build();
            LOGGER.info("Event Endpoint: findEventsFilteredByAttributes" + parameters.toString() );
            return eventService.findAllFiltered(parameters);
        }
    }

    // OK
    @RequestMapping(method = RequestMethod.GET, value = "/artist/{id}")
    public List<EventDTO> findEventsFilteredByArtistID(@PathVariable Long id) {
        LOGGER.info("Event Endpoint: findEventsFilteredByArtistID");
        return eventService.findEventsFilteredByArtistID(id);
    }

}
