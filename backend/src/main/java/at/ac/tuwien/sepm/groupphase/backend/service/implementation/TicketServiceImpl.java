package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.TicketService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;

    public TicketServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Ticket postTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public List<Ticket> findAll() {
        return ticketRepository.findAllByOrderByReservationNumberAsc();
    }

    @Override
    public List<Ticket> findAllFilteredByCustomerAndEvent(String customerName, String eventName) {
        return ticketRepository.findAllFilteredByCustomerAndEvent(customerName, eventName);
    }

    @Override
    public Ticket findOne(Long id) {
        return ticketRepository.findOneByReservationNumber(id).orElseThrow(NotFoundException::new);
    }

}
