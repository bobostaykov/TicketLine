package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ticket.TicketDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.ticket.TicketMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.TicketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(value = "/tickets")
@Api(value = "tickets")
public class TicketEndpoint {
    private final TicketService ticketService;
    private static final Logger LOGGER = LoggerFactory.getLogger(TicketEndpoint.class);

    public TicketEndpoint(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "Create a ticket", authorizations = {@Authorization(value = "apiKey")})
    public TicketDTO create(@RequestBody TicketDTO ticketDTO) {
        LOGGER.info("Create Ticket");
        return ticketService.postTicket(ticketDTO);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get ticket by id", authorizations = {@Authorization(value = "apiKey")})
    public TicketDTO findOne(@PathVariable Long id) {
        return ticketService.findOne(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Get all tickets filtered", authorizations = {@Authorization(value = "apiKey")})
    public List<TicketDTO> findTicketFilteredByCustomerAndEvent(@RequestParam(value = "customerName", required = false) String customerName,
                                                                @RequestParam(value = "eventName", required = false) String eventName) {
        if (customerName == null && eventName == null) {
            return ticketService.findAll();
        } else {
            return ticketService.findAllFilteredByCustomerAndEvent(customerName, eventName);
        }
    }
}
