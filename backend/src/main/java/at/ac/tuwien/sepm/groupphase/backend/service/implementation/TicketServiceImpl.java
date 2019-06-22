package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.datatype.TicketStatus;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.TicketEndpoint;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ticket.TicketDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ticket.TicketPostDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.customer.CustomerMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.show.ShowMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.ticket.TicketMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.TicketSoldOutException;
import at.ac.tuwien.sepm.groupphase.backend.repository.*;
import at.ac.tuwien.sepm.groupphase.backend.service.CustomerService;
import at.ac.tuwien.sepm.groupphase.backend.service.TicketService;
import at.ac.tuwien.sepm.groupphase.backend.service.generator.PDFGenerator;
import at.ac.tuwien.sepm.groupphase.backend.service.ticketExpirationHandler.TicketExpirationHandler;
import com.itextpdf.text.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final CustomerRepository customerRepository;
    private final EventRepository eventRepository;
    private final ShowRepository showRepository;
    private final TicketMapper ticketMapper;
    private final SeatRepository seatRepository;
    private final SectorRepository sectorRepository;
    private final PDFGenerator pdfGenerator;
    private final TicketExpirationHandler ticketExpirationHandler;
    private final ShowMapper showMapper;

    private static final String RECEIPT_PATH = "receipt/";

    private static final Logger LOGGER = LoggerFactory.getLogger(TicketServiceImpl.class);

    public TicketServiceImpl(TicketRepository ticketRepository, CustomerRepository customerRepository,
                             EventRepository eventRepository, TicketMapper ticketMapper,
                             ShowRepository showRepository, SeatRepository seatRepository,
                             SectorRepository sectorRepository, PDFGenerator pdfGenerator,
                             TicketExpirationHandler ticketExpirationHandler, ShowMapper showMapper) {
        this.ticketRepository = ticketRepository;
        this.customerRepository = customerRepository;
        this.eventRepository = eventRepository;
        this.ticketMapper = ticketMapper;
        this.showRepository = showRepository;
        this.seatRepository = seatRepository;
        this.sectorRepository = sectorRepository;
        this.pdfGenerator = pdfGenerator;
        this.ticketExpirationHandler = ticketExpirationHandler;
        this.showMapper = showMapper;
    }

    @Override
    public List<TicketDTO> postTicket(List<TicketPostDTO> ticketPostDTO) throws TicketSoldOutException {
        LOGGER.info("Ticket Service: Create Ticket");
        // Check if any of the requested tickets were already sold or reservated
        for (TicketPostDTO current : ticketPostDTO) {
            if (current.getSeat() != null) {
                if (!this.ticketRepository.findAllByShowAndSeat(this.showRepository.getOne(current.getShow()),
                    this.seatRepository.getOne(current.getSeat())).isEmpty()) {
                    throw new TicketSoldOutException("Ticket for this seat is already sold, please choose another seat");
                }
            }
            if (current.getSector() != null) {
                if (this.ticketRepository.findAllByShowAndSector(this.showRepository.getOne(current.getShow()),
                    this.sectorRepository.getOne(current.getSector())).size() ==
                    this.showRepository.getOne(current.getShow()).getHall().getSeats().size()) {
                    throw new TicketSoldOutException("Tickets for this sector are sold out, please choose another sector");
                }
            }
        }

        // Create each ticket
        List<TicketDTO> created = new ArrayList<>();
        for (TicketPostDTO current : ticketPostDTO) {
            Customer customer = this.customerRepository.getOne(current.getCustomer());
            if (customer == null) {
                throw new NotFoundException("No Customer found with id " + current.getCustomer());
            }
            Show show = this.showRepository.getOne(current.getShow());
            if (show == null) {
                throw new NotFoundException("No Show found with id " + current.getShow());
            }
            if ((current.getSeat() == null && current.getSector() == null) || (current.getSeat() != null && current.getSector() != null)) {
                throw new NotFoundException("Either seat or sector must be given.");
            }
            Seat seat = null;
            Sector sector = null;
            if (current.getSeat() != null) {
                seat = this.seatRepository.findOneById(current.getSeat()).get();
                if (!show.getHall().getSeats().contains(seat)) {
                    throw new NotFoundException("Seat " + seat.getSeatNumber() + " in row " + seat.getSeatRow() +
                        " not found in list of seats for this show!");
                }
            }
            if (current.getSector() != null) {
                sector = this.sectorRepository.getOne(current.getSector());
                if (!show.getHall().getSectors().contains(sector)) {
                    throw new NotFoundException("Sector " + sector.getSectorNumber() +
                        " not found in list of sectors for this show!");
                }
            }
            String uniqueReservationNo = null;
            if (current.getStatus() == TicketStatus.RESERVATED) {
                uniqueReservationNo = UUID.randomUUID().toString();
            }
            Ticket ticket = new Ticket().builder()
                .status(current.getStatus())
                .customer(customer)
                .price(current.getPrice())
                .show(show)
                .seat(seat)
                .sector(sector)
                .reservationNo(uniqueReservationNo)
                .build();
            show.setTicketsSold(show.getTicketsSold() + 1);
            showRepository.save(show);
            /* TODO: test everthing, also test if incrementing ticketSold worked */
            created.add(ticketMapper.ticketToTicketDTO(ticketRepository.save(ticket)));
        }
        return created;
    }

    @Override
    public List<TicketDTO> findAll() {
        LOGGER.info("Ticket Service: Get all tickets");
        ticketExpirationHandler.setAllExpiredReservatedTicketsToStatusExpired();
        return ticketMapper.ticketToTicketDTO(ticketRepository.findAllByOrderByIdAsc());
    }

    @Override
    public TicketDTO findOne(Long id) {
        LOGGER.info("Ticket Service: Find Ticket with id {}", id);
        TicketDTO ticketDTO = ticketMapper.ticketToTicketDTO(ticketRepository.findOneById(id).orElseThrow(NotFoundException::new));
        ticketDTO = ticketExpirationHandler.setExpiredReservatedTicketsToStatusExpired(ticketDTO);
        return ticketDTO;
    }

    @Override
    public TicketDTO changeStatusToSold(Long id) {
        LOGGER.info("Ticket Service: Change Ticket status for ticket with id {} to sold", id);
        TicketDTO ticket = this.findOneReservated(id);
        ticket.setStatus(TicketStatus.SOLD);
        return ticketMapper.ticketToTicketDTO(ticketRepository.save(ticketMapper.ticketDTOToTicket(ticket)));
    }

    @Override
    public TicketDTO findOneReservated(Long id) {
        LOGGER.info("Ticket Service: Find Ticket by id {} with status = {}", id, TicketStatus.RESERVATED);
        TicketDTO ticketDTO = ticketMapper.ticketToTicketDTO(ticketRepository.findOneByIdAndStatus(id, TicketStatus.RESERVATED).orElseThrow(NotFoundException::new));
        return ticketExpirationHandler.setExpiredReservatedTicketsToStatusExpired(ticketDTO);
    }

    @Override
    @Transactional
    public TicketDTO deleteOne(Long id) {
        LOGGER.info("Ticket Service: Delete ticket with id {}", id);
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
        LOGGER.info("Ticket Service: Find all tickets filtered by customer with name {} and event with name {}", customerName, eventName);
        List<Customer> customers = customerRepository.findAllByName(customerName);
        List<Event> events =  eventRepository.findAllByName(eventName);
        List<Show> shows = showRepository.findAllByEvent(events);
        List<Ticket> result1 = new ArrayList<>();
        List<Ticket> result2 = new ArrayList<>();
        if (customerName != null) {
            result1 = ticketRepository.findAllByCustomer(customers);
            //result1 = ticketExpirationHandler.setExpiredReservatedTicketsToStatusExpired(ticketMapper.ticketToTicketDTO(ticketRepository.findAllByCustomer(customers)));
        }
        if (eventName != null) {
            result2 = ticketRepository.findAllByShow(shows);
            //result2 = ticketExpirationHandler.setExpiredReservatedTicketsToStatusExpired(ticketMapper.ticketToTicketDTO(ticketRepository.findAllByShow(shows)));
        }
        List<Ticket> result = this.difference(result1, result2);
        List<TicketDTO> res = ticketExpirationHandler.setExpiredReservatedTicketsToStatusExpired(ticketMapper.ticketToTicketDTO(result));
        return res;
        //return /*ticketExpirationHandler.setExpiredReservatedTicketsToStatusExpired(*/ticketMapper.ticketToTicketDTO(this.difference(result1, result2));
    }

    @Override
    public byte[] getReceipt(List<String> ticketIDs) throws DocumentException, IOException {
        LOGGER.info("Ticket Service: Get receipt PDF for ticket(s) with id(s) " + ticketIDs.toString());
        List<TicketDTO> tickets = ticketMapper.ticketToTicketDTO(ticketRepository.findByIdIn(this.parseListOfIds(ticketIDs)));
        return pdfGenerator.generateReceipt(tickets, false);
    }

    @Override
    @Transactional
    public byte[] deleteAndGetCancellationReceipt(List<String> ticketIDs) throws DocumentException, IOException {
        LOGGER.info("Ticket Service: Delete Ticket(s) with id(s)" + ticketIDs.toString() + " and receive storno receipt");
        List<TicketDTO> tickets = ticketMapper.ticketToTicketDTO(ticketRepository.findByIdIn(this.parseListOfIds(ticketIDs)));
        ticketRepository.deleteByIdIn(this.parseListOfIds(ticketIDs));
        return pdfGenerator.generateReceipt(tickets, true);
    }

    @Override
    public byte[] generateTicketPDF(List<String> ticketIDs) throws DocumentException, IOException, NoSuchAlgorithmException {
        LOGGER.info("Ticket Service: Get a PDF for ticket(s) {}", ticketIDs.toString());
        List<TicketDTO> tickets = ticketExpirationHandler.setExpiredReservatedTicketsToStatusExpired(ticketMapper.ticketToTicketDTO(ticketRepository.findByIdIn(this.parseListOfIds(ticketIDs))));
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
