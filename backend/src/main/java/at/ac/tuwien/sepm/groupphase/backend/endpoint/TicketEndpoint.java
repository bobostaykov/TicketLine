package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.show.ShowDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ticket.TicketDTO;
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

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Get all tickets", authorizations = {@Authorization(value = "apiKey")})
    public List<TicketDTO> findAll() {
        return ticketService.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Delete a ticket", authorizations = {@Authorization(value = "apiKey")})
    public TicketDTO deleteById(@PathVariable Long id) {
        LOGGER.info("Delete Ticket with id " + id);
        return ticketService.deleteOne(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Find Ticket", authorizations = {@Authorization(value = "apiKey")})
    public TicketDTO findById(@PathVariable Long id) {
        LOGGER.info("Find Ticket with id " + id);
        return ticketService.findOne(id);
    }

    @RequestMapping(value = "/buy/{id}", method = RequestMethod.PUT, produces = "application/json")
    @ApiOperation(value = "Buy reservated Ticket", authorizations = {@Authorization(value = "apiKey")})
    public TicketDTO buyReservatedTicket(@PathVariable Long id) {
        LOGGER.info("Buy Ticket with id " + id);
        return ticketService.changeStatusToSold(id);
    }

    @RequestMapping(value = "/reservated/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Find reservated Ticket", authorizations = {@Authorization(value = "apiKey")})
    public TicketDTO findReservatedById(@PathVariable Long id) {
        LOGGER.info("Find reservated Ticket with id " + id);
        return ticketService.findOneReservated(id);
    }

    // PINO's Implementation
    /*
    @RequestMapping(value = "/name", method = RequestMethod.GET)
    @ApiOperation(value = "Get reservated Tickets by customer name and show", authorizations = {@Authorization(value = "apiKey")})
    public List<TicketDTO> findByCustomerNameAndShowWithStatusReservated(@RequestParam(name = "surname") String surname,
                                                                         @RequestParam(name = "firstname") String firstname,
                                                                         @RequestBody ShowDTO showDTO) {
        LOGGER.info("Find reservated Tickets for customer " + firstname + surname + " and show id " + showDTO.getId());
        return ticketService.findByCustomerNameAndShowWithStatusReservated(surname, firstname, showDTO);
    }
     */

    // PINO: added value = "filter" to avoid GET method crash with findAll()
    @RequestMapping(value = "/filter", method = RequestMethod.GET)
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
