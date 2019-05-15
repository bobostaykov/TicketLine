package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ticket.TicketDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.ticket.TicketMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.TicketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/tickets")
@Api(value = "tickets")
public class TicketEndpoint {
    private final TicketService ticketService;
    private final TicketMapper ticketMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(TicketEndpoint.class);

    public TicketEndpoint(TicketService ticketService, TicketMapper ticketMapper) {
        this.ticketService = ticketService;
        this.ticketMapper = ticketMapper;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "Create a ticket", authorizations = {@Authorization(value = "apiKey")})
    public TicketDTO create(@RequestBody TicketDTO ticketDTO) {
        LOGGER.info("Create Ticket");
        return ticketMapper.ticketToTicketDTO(ticketService.postTicket(ticketMapper.ticketDTOToTicket(ticketDTO)));
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Get all tickets", authorizations = {@Authorization(value = "apiKey")})
    public List<TicketDTO> findAll() {
        return ticketMapper.ticketToTicketDTO(ticketService.findAll());
    }
}
