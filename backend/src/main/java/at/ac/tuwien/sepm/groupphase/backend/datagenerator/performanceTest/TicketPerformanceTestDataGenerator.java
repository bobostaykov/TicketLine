package at.ac.tuwien.sepm.groupphase.backend.datagenerator.performanceTest;

import at.ac.tuwien.sepm.groupphase.backend.datatype.TicketStatus;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.repository.CustomerRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShowRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Profile("generatePerformanceTestData")
@Component
public class TicketPerformanceTestDataGenerator extends PerformanceTestDataGenerator {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    Faker faker = new Faker();

    private final TicketRepository ticketRepository;
    private final CustomerRepository customerRepository;
    private final ShowRepository showRepository;

    private final static int NUM_OF_TICKETS = 2;

    public TicketPerformanceTestDataGenerator(TicketRepository ticketRepository,
                                              CustomerRepository customerRepository,
                                              ShowRepository showRepository){
        this.ticketRepository = ticketRepository;
        this.customerRepository = customerRepository;
        this.showRepository = showRepository;
    }

    @Override
    public void generate(){
        if(ticketRepository.count() > 0){
            LOGGER.info("Tickets already generated");
        }else {
            LOGGER.info("Generating {} tickets", NUM_OF_TICKETS);
            List<Ticket> tickets = new ArrayList<>();
            for(Long id = 1L; id <= NUM_OF_TICKETS; id++) {
                tickets.add(Ticket.builder()
                    .id(id)
                    .customer(customerRepository.getOne(customMod(id, NUM_OF_CUSTOMERS)))
                    .show(showRepository.getOne(customMod(id, NUM_OF_SHOWS)))
                    .status(id % 2 == 0 ? TicketStatus.SOLD : TicketStatus.RESERVATED)
                    //.seatNumber(customModInt(id, NUM_OF_SEAT_ROWS_PER_HALL))
                    //.rowNumber(Math.toIntExact((id-1) / NUM_OF_SEAT_ROWS_PER_HALL + 1))
                    .price(TICKET_PRICE)
                    .build());
            }
            ticketRepository.saveAll(tickets); // TODO: use service in order to increment the soldTickets field for the show
        }
    }
}
