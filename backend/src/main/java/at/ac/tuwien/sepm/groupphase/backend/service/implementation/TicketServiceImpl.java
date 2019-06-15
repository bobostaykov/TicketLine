package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.datatype.TicketStatus;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ticket.TicketDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Customer;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.customer.CustomerMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.show.ShowMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.ticket.TicketMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.CustomerRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShowRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.CustomerService;
import at.ac.tuwien.sepm.groupphase.backend.service.TicketService;
import at.ac.tuwien.sepm.groupphase.backend.service.generator.PDFGenerator;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@ConfigurationProperties("receipt")
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final CustomerRepository customerRepository;
    private final EventRepository eventRepository;
    private final ShowRepository showRepository;
    private final TicketMapper ticketMapper;
    private final ShowMapper showMapper;
    private final CustomerMapper customerMapper;
    private final CustomerService customerService;
    private final PDFGenerator pdfGenerator;

    private static final String RECEIPT_PATH = "receipt/";

    @Value("${receipt.address}")
    private static String TICKETLINE_ADDRESS;

    public TicketServiceImpl(TicketRepository ticketRepository, CustomerRepository customerRepository,
                             EventRepository eventRepository, TicketMapper ticketMapper, ShowMapper showMapper,
                             CustomerMapper customerMapper, CustomerService customerService,
                             ShowRepository showRepository, PDFGenerator pdfGenerator) {
        this.ticketRepository = ticketRepository;
        this.customerRepository = customerRepository;
        this.eventRepository = eventRepository;
        this.ticketMapper = ticketMapper;
        this.showMapper = showMapper;
        this.customerMapper = customerMapper;
        this.customerService = customerService;
        this.showRepository = showRepository;
        this.pdfGenerator = pdfGenerator;
    }

    @Override
    public List<TicketDTO> postTickets(List<TicketDTO> tickets) {
        return ticketMapper.ticketToTicketDTO(ticketRepository.saveAll(ticketMapper.ticketDTOToTicket(tickets)));

    }

    @Override
    public List<TicketDTO> findAll() {
        return ticketMapper.ticketToTicketDTO(ticketRepository.findAllByOrderByIdAsc());
    }

    @Override
    public TicketDTO findOne(Long id) {
        return ticketMapper.ticketToTicketDTO(ticketRepository.findOneById(id).orElseThrow(NotFoundException::new));
    }

    @Override
    public TicketDTO changeStatusToSold(Long id) {
        TicketDTO ticket = this.findOneReservated(id);
        ticket.setStatus(TicketStatus.SOLD);
        return ticketMapper.ticketToTicketDTO(ticketRepository.save(ticketMapper.ticketDTOToTicket(ticket)));
    }

    @Override
    public TicketDTO findOneReservated(Long id) {
        return ticketMapper.ticketToTicketDTO(ticketRepository.findOneByIdAndStatus(id, TicketStatus.RESERVATED).orElseThrow(NotFoundException::new));
    }

    @Override
    @Transactional
    public TicketDTO deleteOne(Long id) {
        Optional<Ticket> ticket = ticketRepository.findOneById(id);
        if (ticket.isPresent()) {
            ticketRepository.delete(ticket.get());
        }
        else
            throw new NotFoundException("Ticket with id " + id + " not found.");
        return ticketMapper.ticketToTicketDTO(ticket.get());
    }

    @Override
    public List<TicketDTO> findAllFilteredByCustomerAndEvent(String customerName, String eventName) {
        List<Customer> customers = customerRepository.findAllByName(customerName);
        List<Event> events =  eventRepository.findAllByName(eventName);
        List<Show> shows = showRepository.findAllByEvent(events);
        List<Ticket> result1 = new ArrayList<>();
        List<Ticket> result2 = new ArrayList<>();
        if (customerName != null) {
            result1 = ticketRepository.findAllByCustomer(customers);
        }
        if (eventName != null) {
            result2 = ticketRepository.findAllByShow(shows);
        }
        return ticketMapper.ticketToTicketDTO(this.difference(result1, result2));
    }

    @Override
    public MultipartFile getReceipt(List<String> ticketIDs) throws DocumentException, IOException {
        List<TicketDTO> tickets = ticketMapper.ticketToTicketDTO(ticketRepository.findByIdIn(this.parseListOfIds(ticketIDs)));
        return pdfGenerator.generateReceipt(tickets, false);
    }

    @Override
    @Transactional
    public MultipartFile deleteAndGetCancellationReceipt(List<String> ticketIDs) throws DocumentException, IOException{
        List<TicketDTO> tickets = ticketMapper.ticketToTicketDTO(ticketRepository.findByIdIn(this.parseListOfIds(ticketIDs)));
        ticketRepository.deleteByIdIn(this.parseListOfIds(ticketIDs));
        return pdfGenerator.generateReceipt(tickets, true);
    }

    @Override
    public MultipartFile generateTicketPDF(List<TicketDTO> tickets) throws DocumentException, IOException, NoSuchAlgorithmException {
        return pdfGenerator.generateTicketPDF(tickets);
    }

    /**
     * Parse a list of ids
     *
     * @param stringIds List of ticket ids as strings
     * @return a list ticket ids as Long
     */
    private List<Long> parseListOfIds(List<String> stringIds) {
        List<Long> ids = new ArrayList<>();
        for (String i:
            stringIds) {
            ids.add(Long.parseLong(i));
        }
        return ids;
    }

    /**
     * Get the conjunction of tickets in list 1 and list 2
     *
     * @param list1 list of tickets
     * @param list2 list of tickets
     * @return conjunction of list1 and list2
     */
    private List<Ticket> difference(List<Ticket> list1, List<Ticket> list2) {
        List<Ticket> result = new ArrayList<>();
        for (Ticket ticket : list1) {
            if(list2.contains(ticket)) {
                result.add(ticket);
            }
        }
        return result;
    }

    // PINOS IMPLEMENTATION
    /*
    @Override
    public List<TicketDTO> findByCustomerNameAndShowWithStatusReservated(String surname, String firstname, ShowDTO showDTO) {
        List<CustomerDTO> customers = customerService.findCustomersFiltered(null, surname, firstname, null, null);
        if (customers.isEmpty())
            throw new NotFoundException("No Customer with name " + firstname + surname + " found.");
        List<TicketDTO> tickets = new ArrayList<>();
        for (CustomerDTO c: customers) {
            List<Ticket> ticketsTemp = ticketRepository.findAllByCustomerAndShowWithStatusReservated(customerMapper.customerDTOToCustomer(c), showMapper.showDTOToShow(showDTO));
            for (Ticket t: ticketsTemp) {
                tickets.add(ticketMapper.ticketToTicketDTO(t));
            }
        }
        return tickets;
    }
    */

}
