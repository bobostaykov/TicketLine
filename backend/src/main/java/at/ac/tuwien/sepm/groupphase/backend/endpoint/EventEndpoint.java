package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.datatype.EventType;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.event.EventDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.event.EventTicketsDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.event.EventTicketsMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping(value = "/events")
@Api(value = "events")
public class EventEndpoint {

    private final EventService eventService;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public EventEndpoint(EventService eventService) {
        this.eventService = eventService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/topten")
    @ApiOperation(value = "Get top 10 events", authorizations = {@Authorization(value = "apiKey")})
    public List<EventTicketsDTO> findTopTenEvents(@RequestParam(value = "months") String months, @RequestParam(value = "categories") String categories) {
        LOGGER.info("Get top 10 events");
        Set<String> monthsSet = new HashSet<>(Arrays.asList(months.split(",")));
        Set<EventType> categoriesSet = new HashSet<>();

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
    public List<EventDTO> findAll() {
        LOGGER.info("Get all events");
        try {
            return eventService.findAll();
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

}
