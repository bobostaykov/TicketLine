package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShowRepository;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@Profile("generateData")
@Component
public class ShowDataGenerator implements DataGenerator{

    private static final Logger LOGGER = LoggerFactory.getLogger(ShowDataGenerator.class);

    private final ShowRepository showRepository;
    private final EventRepository eventRepository;
    //private final HallRepository hallRepository; + .hall(hallRepository.getOne())

    private final Faker faker;
    private final DateTimeFormatter formatter;

    public ShowDataGenerator(ShowRepository showRepository, EventRepository eventRepository) {
        this.showRepository = showRepository;
        this.eventRepository = eventRepository;
        faker = new Faker();
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    }

    @Override
    public void generate() {
        if (showRepository.count() > 0) {
            LOGGER.info("Shows already generated");
        } else {
            LOGGER.info("Generating Show entries");
            showRepository.save(Show.builder().id(1L).event(eventRepository.getOne(1L)).durationInMinutes(180).ticketsSold(10000L).dateTime(LocalDateTime.parse("09-10-2019 10:30", formatter)).build());
            showRepository.save(Show.builder().id(2L).event(eventRepository.getOne(2L)).durationInMinutes(200).ticketsSold(2000L).dateTime(LocalDateTime.parse("15-11-2019 20:30", formatter)).build());
            showRepository.save(Show.builder().id(3L).event(eventRepository.getOne(3L)).durationInMinutes(100).ticketsSold(1500L).dateTime(LocalDateTime.parse("14-08-2019 17:30", formatter)).build());
            showRepository.save(Show.builder().id(4L).event(eventRepository.getOne(4L)).durationInMinutes(50).ticketsSold(150L).dateTime(LocalDateTime.parse("07-07-2019 19:00", formatter)).build());
            showRepository.save(Show.builder().id(5L).event(eventRepository.getOne(5L)).durationInMinutes(60).ticketsSold(200L).dateTime(LocalDateTime.parse("01-01-2019 19:30", formatter)).build());

            showRepository.save(Show.builder().id(6L).event(eventRepository.getOne(6L)).durationInMinutes(110).ticketsSold(1000L).dateTime(LocalDateTime.parse("17-09-2029 21:00", formatter)).build());
            showRepository.save(Show.builder().id(7L).event(eventRepository.getOne(7L)).durationInMinutes(120).ticketsSold(500L).dateTime(LocalDateTime.parse("19-02-2019 23:00", formatter)).build());
            showRepository.save(Show.builder().id(8L).event(eventRepository.getOne(8L)).durationInMinutes(40).ticketsSold(400L).dateTime(LocalDateTime.parse("04-04-2019 23:30", formatter)).build());
            showRepository.save(Show.builder().id(9L).event(eventRepository.getOne(9L)).durationInMinutes(130).ticketsSold(500L).dateTime(LocalDateTime.parse("22-06-2019 14:30", formatter)).build());
            showRepository.save(Show.builder().id(10L).event(eventRepository.getOne(10L)).durationInMinutes(130).ticketsSold(500L).dateTime(LocalDateTime.parse("29-12-2019 15:30", formatter)).build());

            showRepository.save(Show.builder().id(11L).event(eventRepository.getOne(1L)).durationInMinutes(100).ticketsSold(2000L).dateTime(LocalDateTime.parse("16-07-2019 10:30", formatter)).build());
            showRepository.save(Show.builder().id(12L).event(eventRepository.getOne(1L)).durationInMinutes(110).ticketsSold(1800L).dateTime(LocalDateTime.parse("04-12-2019 12:00", formatter)).build());
            showRepository.save(Show.builder().id(13L).event(eventRepository.getOne(3L)).durationInMinutes(120).ticketsSold(1200L).dateTime(LocalDateTime.parse("03-11-2019 12:30", formatter)).build());
            showRepository.save(Show.builder().id(14L).event(eventRepository.getOne(4L)).durationInMinutes(90).ticketsSold(1400L).dateTime(LocalDateTime.parse("17-02-2019 13:00", formatter)).build());
            showRepository.save(Show.builder().id(15L).event(eventRepository.getOne(4L)).durationInMinutes(70).ticketsSold(100L).dateTime(LocalDateTime.parse("23-02-2019 15:00", formatter)).build());

            showRepository.save(Show.builder().id(16L).event(eventRepository.getOne(5L)).durationInMinutes(70).ticketsSold(200L).dateTime(LocalDateTime.parse("28-07-2019 17:30", formatter)).build());
            showRepository.save(Show.builder().id(17L).event(eventRepository.getOne(6L)).durationInMinutes(80).ticketsSold(400L).dateTime(LocalDateTime.parse("30-08-2019 15:00", formatter)).build());
            showRepository.save(Show.builder().id(18L).event(eventRepository.getOne(6L)).durationInMinutes(80).ticketsSold(100L).dateTime(LocalDateTime.parse("30-03-2019 11:00", formatter)).build());
            showRepository.save(Show.builder().id(19L).event(eventRepository.getOne(7L)).durationInMinutes(80).ticketsSold(500L).dateTime(LocalDateTime.parse("11-05-2019 19:00", formatter)).build());
            showRepository.save(Show.builder().id(20L).event(eventRepository.getOne(7L)).durationInMinutes(80).ticketsSold(500L).dateTime(LocalDateTime.parse("06-01-2019 21:30", formatter)).build());

            showRepository.save(Show.builder().id(21L).event(eventRepository.getOne(7L)).durationInMinutes(190).ticketsSold(12000L).dateTime(LocalDateTime.parse("05-02-2019 21:00", formatter)).build());
            showRepository.save(Show.builder().id(22L).event(eventRepository.getOne(9L)).durationInMinutes(200).ticketsSold(25000L).dateTime(LocalDateTime.parse("19-03-2019 23:30", formatter)).build());
            showRepository.save(Show.builder().id(23L).event(eventRepository.getOne(9L)).durationInMinutes(150).ticketsSold(1000L).dateTime(LocalDateTime.parse("24-04-2019 23:30", formatter)).build());
            showRepository.save(Show.builder().id(24L).event(eventRepository.getOne(10L)).durationInMinutes(160).ticketsSold(11000L).dateTime(LocalDateTime.parse("22-08-2019 23:00", formatter)).build());
            showRepository.save(Show.builder().id(25L).event(eventRepository.getOne(10L)).durationInMinutes(130).ticketsSold(1800L).dateTime(LocalDateTime.parse("23-11-2019 18:30", formatter)).build());
        }
    }
}