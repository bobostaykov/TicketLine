package at.ac.tuwien.sepm.groupphase.backend.datagenerator.demo;

import at.ac.tuwien.sepm.groupphase.backend.datatype.TicketStatus;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.*;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Profile("generateData")
@Component
public class TicketDataGenerator implements DataGenerator {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    Faker faker = new Faker();

    private final TicketRepository ticketRepository;
    private final CustomerRepository customerRepository;
    private final ShowRepository showRepository;
    private final SeatRepository seatRepository;

    private long NUM_OF_TICKETS;
    private int NUM_OF_SECTORS_PER_HALL = 5;

    public TicketDataGenerator(TicketRepository ticketRepository,
                               CustomerRepository customerRepository,
                               ShowRepository showRepository,
                               SeatRepository seatRepository){
        this.ticketRepository = ticketRepository;
        this.customerRepository = customerRepository;
        this.showRepository = showRepository;
        this.seatRepository = seatRepository;
    }

    @Transactional
    @Override
    public void generate(){
        if(ticketRepository.count() > 0){
            LOGGER.info("Tickets already generated");
        }else {
            LOGGER.info("Generating tickets");
            Random randomGenerator = new Random();
            List<Ticket> tickets = new ArrayList<>();
            Optional<Show> showOptional;
            Long numOfShows = showRepository.count();
            long numOfCustomers = customerRepository.count();
            Long numOfSeats = seatRepository.count();
            NUM_OF_TICKETS = numOfShows * 5;
            for(Long showId = 1L; showId <= numOfShows; showId++) {
                Show show = showRepository.getOne(showId);
                long numOfTicketsPerShow = NUM_OF_TICKETS/numOfShows;
                for(int i = 0; i < numOfTicketsPerShow; i++) {
                    Ticket ticket = Ticket.builder()
                        .id(showId)
                        .customer(customerRepository.getOne(customMod(showId, (int) numOfCustomers)))
                        .show(show)
                        .status(showId % 6 == 0 ? TicketStatus.RESERVED : TicketStatus.SOLD)
                        .seat(!show.getHall().getSeats().isEmpty() ? show.getHall().getSeats().get(i) : null)
                        .sector(!show.getHall().getSectors().isEmpty() ? show.getHall().getSectors().get(customModInt((long)i, NUM_OF_SECTORS_PER_HALL)) : null)
                        .price(faker.random().nextDouble()*50)
                        .reservationNo(UUID.randomUUID().toString())
                        .build();
                    if (ticket.getStatus() == TicketStatus.SOLD)
                        showRepository.incrementSoldTickets(show.getId());
                    tickets.add(ticket);
                }
            }
            ticketRepository.saveAll(tickets); // TODO: use service in order to increment the soldTickets field for the show
        }
    }

}
