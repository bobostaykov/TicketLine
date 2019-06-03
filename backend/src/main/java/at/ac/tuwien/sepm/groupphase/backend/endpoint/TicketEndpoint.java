package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.show.ShowDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ticket.TicketDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.ticket.TicketMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.TicketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/{id]", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete a ticket", authorizations = {@Authorization(value = "apiKey")})
    public TicketDTO deleteById(@PathVariable Long id) {
        LOGGER.info("Delete Ticket with id " + id);
        return ticketMapper.ticketToTicketDTO(ticketService.deleteOne(id));
    }

    @RequestMapping(value = "/{id]", method = RequestMethod.GET)
    @ApiOperation(value = "Find Ticket", authorizations = {@Authorization(value = "apiKey")})
    public TicketDTO findById(@PathVariable Long id) {
        LOGGER.info("Find Ticket with id " + id);
        return ticketMapper.ticketToTicketDTO(ticketService.findOne(id));
    }

    @RequestMapping(value = "/name", method = RequestMethod.GET)
    @ApiOperation(value = "Get Tickets by customer name and show", authorizations = {@Authorization(value = "apiKey")})
    public List<TicketDTO> findByCustomerNameAndShow(@RequestParam(name = "customerName") String customerName, @RequestBody ShowDTO showDTO) {
        LOGGER.info("Find Tickets for customer " + customerName + " and show id " + showDTO.getId());
        return ticketService.findByCustomerNameAndShow(customerName, showDTO);
    }

}
