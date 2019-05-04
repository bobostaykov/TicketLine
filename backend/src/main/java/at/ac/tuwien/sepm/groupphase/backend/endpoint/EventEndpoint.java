package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.event.EventDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.event.EventMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/events")
@Api(value = "events")
public class EventEndpoint {

    private final EventService eventService;
    private final EventMapper eventMapper;

    public EventEndpoint(EventService eventService, EventMapper eventMapper) {
        this.eventService = eventService;
        this.eventMapper = eventMapper;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/topten")
    @ApiOperation(value = "Get top 10 events", authorizations = {@Authorization(value = "apiKey")})
    public List<EventDTO> findTopTenEvents() {
        List<EventDTO> topTen = new ArrayList<>();
        for (Event event : eventService.findTopTenEvents()) {
            topTen.add(eventMapper.eventToEventDTO(event));
        }
        return topTen;
    }

}
