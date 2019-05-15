package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.entity.Customer;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.CustomerRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.TicketService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final CustomerRepository customerRepository;
    private final EventRepository eventRepository;

    public TicketServiceImpl(TicketRepository ticketRepository, CustomerRepository customerRepository,
                             EventRepository eventRepository) {
        this.ticketRepository = ticketRepository;
        this.customerRepository = customerRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public Ticket postTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public List<Ticket> findAll() {
        return ticketRepository.findAllByOrderByReservationNumberAsc();
    }

    // NOT WORKING
    @Override
    public List<Ticket> findAllFilteredByCustomerAndEvent(String customerName, String eventName) {
        /*
        List<Customer> customers = customerRepository.findAllByName(customerName);
        List<Event> events =  eventRepository.findAllByName(eventName);
        return ticketRepository.findAllFilteredByCustomerAndEvent(customers, events);
         */
        return null;
    }

    @Override
    public Ticket findOne(Long id) {
        return ticketRepository.findOneByReservationNumber(id).orElseThrow(NotFoundException::new);
    }

}
