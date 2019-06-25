package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ticket.TicketDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ticket.TicketPostDTO;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.TicketSoldOutException;
import at.ac.tuwien.sepm.groupphase.backend.service.TicketService;
import com.itextpdf.text.DocumentException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import io.swagger.models.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping(value = "/tickets")
@Api(value = "tickets")
public class TicketEndpoint {
    private final TicketService ticketService;
    private static final Logger LOGGER = LoggerFactory.getLogger(TicketEndpoint.class);
    private static final String PDF_ENDPOINT = "/printable";

    public TicketEndpoint(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "Create a ticket", authorizations = {@Authorization(value = "apiKey")})
    public List<TicketDTO> create(@RequestBody List<TicketPostDTO> ticketPostDTO) {
        LOGGER.info("Create Ticket");
        try {
            return ticketService.postTicket(ticketPostDTO);
        } catch (TicketSoldOutException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Delete a ticket", authorizations = {@Authorization(value = "apiKey")})
    public TicketDTO deleteById(@PathVariable Long id) {
        LOGGER.info("Delete Ticket with id " + id);
        return ticketService.deleteOne(id);
    }

    @RequestMapping(value = PDF_ENDPOINT + "/cancellation", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete Tickets by id and receive storno receipt", authorizations = {@Authorization(value = "apiKey")})
    public ResponseEntity<byte[]> deleteAndGetStornoReceipt(@RequestParam List<String> tickets) throws IOException, DocumentException {
        LOGGER.info("Delete Ticket(s) with id(s)" + tickets.toString() + " and receive storno receipt");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        return new ResponseEntity<>(ticketService.deleteAndGetCancellationReceipt(tickets), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Find Ticket by id", authorizations = {@Authorization(value = "apiKey")})
    public TicketDTO findById(@PathVariable Long id) {
        LOGGER.info("Ticket Endpoint: Find Ticket with id " + id);
        return ticketService.findOne(id);
    }

    @RequestMapping(value = "/buy/{id}", method = RequestMethod.PUT, produces = "application/json")
    @ApiOperation(value = "Buy reservated Ticket by id", authorizations = {@Authorization(value = "apiKey")})
    public TicketDTO buyReservatedTicket(@PathVariable Long id) {
        LOGGER.info("Ticket Endpoint: Buy Ticket with id " + id);
        return ticketService.changeStatusToSold(id);
    }

    @RequestMapping(value = "/buy", method = RequestMethod.POST, produces = "application/json")
    @ApiOperation(value = "Buy multiple reservated Tickets by id", authorizations = {@Authorization(value = "apiKey")})
    public List<TicketDTO> buyMultipleReservatedTickets(@RequestBody List<Long> tickets){
        LOGGER.info("buy tickets with ids" + tickets.toString());
        try {
            return ticketService.changeStatusToSold(tickets);
        } catch (TicketSoldOutException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @RequestMapping(value = "/reserved/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Find reserved Ticket by id", authorizations = {@Authorization(value = "apiKey")})
    public TicketDTO findReservedById(@PathVariable Long id) {
        LOGGER.info("Ticket Endpoint: Find reserved Ticket with id " + id);
        return ticketService.findOneReserved(id);
    }

    // PINO: added value = "filter" to avoid GET method crash with findAll()
    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    @ApiOperation(value = "Find all tickets filtered by customer name and event name", authorizations = {@Authorization(value = "apiKey")})
    public Page<TicketDTO> findTicketFilteredByCustomerAndEvent(@RequestParam(value = "customerName", required = false) @NotNull String customerName,
                                                                @RequestParam(value = "eventName", required = false) @NotNull String eventName,
                                                                @RequestParam(value = "number", required = false) @NotNull String number,
                                                                @RequestParam(value = "page", required = true) @PositiveOrZero Integer page,
                                                                @RequestParam(value = "pageSize", required = false) @PositiveOrZero Integer pageSize,
                                                                @RequestParam(value = "reserved", required = false) Boolean reserved){
        LOGGER.info("Ticket Endpoint: Find all tickets filtered by customer with name {} and event with name {} or with reservationNumber{}", customerName, eventName, number);
        if(number != null){
            return ticketService.findAllFilteredByReservationNumber(number, reserved, page, pageSize);
        }
        if (customerName == null && eventName == null) {
            return ticketService.findAll(page, pageSize);
        } else {
            return ticketService.findAllFilteredByCustomerAndEvent(customerName, eventName, reserved, page, pageSize);
        }
    }

    @RequestMapping(value = PDF_ENDPOINT + "/receipt", method = RequestMethod.GET)
    @ApiOperation(value = "Get receipt PDF for list of tickets", authorizations = {@Authorization(value = "apiKey")})
    public ResponseEntity<byte[]> getReceiptPDF(@RequestParam List<String> tickets) throws IOException, DocumentException {
        LOGGER.info("Ticket Endpoint: Get receipt PDF for ticket(s) with id(s) " + tickets.toString());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        return new ResponseEntity<>(ticketService.getReceipt(tickets), headers, HttpStatus.OK);
    }

    @RequestMapping(value = PDF_ENDPOINT + "/ticket", method = RequestMethod.GET)
    @ApiOperation(value = "Get printable PDF for list of tickets", authorizations = {@Authorization(value = "apiKey")})
    public ResponseEntity<byte[]> getTicketPDF(@RequestParam List<String> tickets) throws DocumentException, NoSuchAlgorithmException, IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        return new ResponseEntity<>(ticketService.generateTicketPDF(tickets), headers, HttpStatus.OK);
    }
}
