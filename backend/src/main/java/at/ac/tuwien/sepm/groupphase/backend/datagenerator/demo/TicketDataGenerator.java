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

    public TicketDataGenerator(TicketRepository ticketRepository,
                               CustomerRepository customerRepository,
                               ShowRepository showRepository,
                               SeatRepository seatRepository){
        this.ticketRepository = ticketRepository;
        this.customerRepository = customerRepository;
        this.showRepository = showRepository;
        this.seatRepository = seatRepository;
    }

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
            Long numOfCustomers = customerRepository.count();
            Long numOfSeats = seatRepository.count();
            NUM_OF_TICKETS = numOfShows * 5;
            for (Long i = 1L; i <= NUM_OF_TICKETS; i++) {
                Long adder = 0L;
                do {
                    showOptional = showRepository.findById(((i+randomGenerator.nextLong()) % numOfShows) + 1);
                    adder++;
                } while (showOptional.isEmpty());
                Show show = showOptional.get();
                show.setTicketsSold(show.getTicketsSold() + 1);
                show = showRepository.save(show);
                tickets.add(Ticket.builder()
                    .reservationNo(UUID.randomUUID().toString())
                    .customer(customerRepository.getOne(((i-1) % numOfCustomers) + 1))
                    .show(show)
                    .price(faker.random().nextDouble()*50)
                    .seat(seatRepository.getOne(((i-1) % numOfSeats) + 1))
                    .sector(null)
                    .status(i % 6 == 0 ? TicketStatus.RESERVATED : TicketStatus.SOLD)
                    .reservationNo("A1234"+ i)
                    .build());
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
