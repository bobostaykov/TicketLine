package at.ac.tuwien.sepm.groupphase.backend.datagenerator.performanceTest;

import at.ac.tuwien.sepm.groupphase.backend.datatype.TicketStatus;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.repository.CustomerRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShowRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Profile("generatePerformanceTestData")
@Component
public class TicketPerformanceTestDataGenerator extends PerformanceTestDataGenerator {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    Faker faker = new Faker();

    private final TicketRepository ticketRepository;
    private final CustomerRepository customerRepository;
    private final ShowRepository showRepository;

    public TicketPerformanceTestDataGenerator(TicketRepository ticketRepository,
                                              CustomerRepository customerRepository,
                                              ShowRepository showRepository){
        this.ticketRepository = ticketRepository;
        this.customerRepository = customerRepository;
        this.showRepository = showRepository;
    }

    @Transactional
    @Override
    public void generate(){
        if(ticketRepository.count() > 0){
            LOGGER.info("Tickets already generated");
        }else {
            LOGGER.info("Generating {} tickets", NUM_OF_TICKETS);
            Random randomGenerator = new Random();
            List<Ticket> tickets = new ArrayList<>();
            Optional<Show> showOptional;
            for(Long showId = 1L; showId <= NUM_OF_SHOWS; showId++) {
                Show show = showRepository.getOne(customMod(showId, NUM_OF_SHOWS));
                int numOfTicketsPerShow = NUM_OF_TICKETS/NUM_OF_SHOWS;
                /*show.setTicketsSold(show.getTicketsSold() + numOfTicketsPerShow);
                show = showRepository.save(show);*/
                for(int i = 0; i < numOfTicketsPerShow; i++) {
                    Ticket ticket = Ticket.builder()
                        .id(showId)
                        .customer(customerRepository.getOne(customMod(showId, NUM_OF_CUSTOMERS)))
                        .show(show)
                        .status(showId % 6 == 0 ? TicketStatus.RESERVED : TicketStatus.SOLD)
                        .seat(!show.getHall().getSeats().isEmpty() ? show.getHall().getSeats().get(i) : null)
                        .sector(!show.getHall().getSectors().isEmpty() ? show.getHall().getSectors().get(customModInt((long)i, NUM_OF_SECTORS_PER_HALL)) : null)
                        .price(TICKET_PRICE)
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
