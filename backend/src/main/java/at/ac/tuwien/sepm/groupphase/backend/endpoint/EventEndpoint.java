package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.datatype.EventType;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.event.EventDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.event.EventTicketsDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.searchParameters.EventSearchParametersDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.event.EventTicketsMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.apache.logging.log4j.spi.LoggerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping(value = "/events")
@Api(value = "events")
public class EventEndpoint {

    private final EventService eventService;
    private final EventTicketsMapper eventTicketsMapper;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public EventEndpoint(EventService eventService, EventTicketsMapper eventTicketsMapper) {
        this.eventService = eventService;
        this.eventTicketsMapper = eventTicketsMapper;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/topten")
    @ApiOperation(value = "Get top 10 events", authorizations = {@Authorization(value = "apiKey")})
    public List<EventTicketsDTO> findTopTenEvents(@RequestParam(value = "months") String months, @RequestParam(value = "categories") String categories) {
        Set<String> monthsSet = new HashSet<>(Arrays.asList(months.split(",")));
        Set<EventType> categoriesSet = new HashSet<>();
        List<EventTicketsDTO> topTen = new ArrayList<>();

        for (String s : categories.split(",")) {
            categoriesSet.add(EventType.valueOf(s));
        }

        for (EventTicketsDTO eventTicketsDTO : eventService.findTopTenEvents(monthsSet, categoriesSet)) {
            topTen.add(eventTicketsDTO);
        }
        return topTen;
    }

    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    @ApiOperation(value = "Get all events filtered by specified attributes", authorizations = {@Authorization(value = "apiKey")})
    public List<EventDTO> findAllShowsFiltered(
        @RequestParam(value="name", required = false) String name,
        @RequestParam(value="duration", required = false) Integer durationInMinutes,
        @RequestParam(value = "content", required = false) String content,
        @RequestParam(value = "artistName", required = false) String artistName) {
        EventSearchParametersDTO parameters = EventSearchParametersDTO.builder().setName(name).setContent(content).setDurationInMinutes(durationInMinutes).setArtistName(artistName).build();
        LOGGER.info("getting all events filtered by parameters: "+ parameters.toString());
        return (eventService.findAllFiltered(parameters));
    }
}
/*
    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Get all events", authorizations = {@Authorization(value = "apiKey")})
    public List<EventDTO> findAll() {
        return eventService.findAll();
    }

}*/
