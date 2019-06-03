package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

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
import java.util.Arrays;
import java.util.List;

@Profile("generateData")
@Component
public class TicketDataGenerator implements DataGenerator {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    Faker faker = new Faker();

    private final TicketRepository ticketRepository;
    private final CustomerRepository customerRepository;
    private final ShowRepository showRepository;

    private final static int NUM_OF_TICKETS = 2;

    public TicketDataGenerator(TicketRepository ticketRepository,
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
            LOGGER.info("Generating tickets");
            List<Ticket> tickets = new ArrayList<>();
            for (Long i = 1L; i <= NUM_OF_TICKETS; i++) {
                tickets.add(Ticket.builder().customer(customerRepository.getOne(i)).show(showRepository.getOne(i)).price(faker.random().nextDouble()).rowNumber(faker.random().nextInt(1, 100).intValue()).seatNumber(faker.random().nextInt(1, 100).intValue()).status("reservated").build());
            }
            /*Long id = 1L, cn = 1L, sn = 1L;
            Customer customer1 = Customer.builder().id(id++).name(faker.name().lastName()).firstname(faker.name().firstName()).email(faker.bothify("????##@gmail.com")).birthday(faker.date().birthday().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate()).build();
            Customer customer2 = Customer.builder().id(id++).name(faker.name().lastName()).firstname(faker.name().firstName()).email(faker.bothify("????##@gmail.com")).birthday(faker.date().birthday().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate()).build();*/

            ticketRepository.saveAll(tickets);
        }
    }
}
