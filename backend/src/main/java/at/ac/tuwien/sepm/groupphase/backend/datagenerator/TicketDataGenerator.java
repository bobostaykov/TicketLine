package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.repository.CustomerRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShowRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Profile("generateData")
@Component
public class TicketDataGenerator implements DataGenerator {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private static final Double TICKET_PRICE = 100.0;
    private static final int SECTOR_NUMBER = 1;
    private static final String TICKET_STATUS_RESERVATED = "RESERVATION";
    private static final String TICKET_STATUS_SOLD = "BUY";

    private final ShowRepository showRepository;
    private final TicketRepository ticketRepository;
    private final CustomerRepository customerRepository;

    public TicketDataGenerator(ShowRepository showRepository, TicketRepository ticketRepository, CustomerRepository customerRepository){
        this.showRepository = showRepository;
        this.ticketRepository = ticketRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public void generate(){
        if(ticketRepository.count() > 0){
            LOGGER.info("Tickets already generated");
        }else {
            LOGGER.info("Generating tickets");
            Long rn = 1L, cn = 1L, sn = 1L;
            Ticket ticket1 = Ticket.builder().reservationNumber(rn++).customer(customerRepository.getOne(cn++)).show(showRepository.getOne(sn++)).price(TICKET_PRICE).sectorNumber(SECTOR_NUMBER).status(TICKET_STATUS_RESERVATED).build();
            Ticket ticket2 = Ticket.builder().reservationNumber(rn++).customer(customerRepository.getOne(cn++)).show(showRepository.getOne(sn++)).price(TICKET_PRICE).sectorNumber(SECTOR_NUMBER).status(TICKET_STATUS_SOLD).build();

            ticketRepository.saveAll(Arrays.asList(ticket1, ticket2));
        }
    }
}
