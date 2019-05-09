package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShowRepository;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

@Profile("generateData")
@Component
public class ShowDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageDataGenerator.class);
    private static final int NUMBER_OF_SHOWS_TO_GENERATE = 25;

    private final ShowRepository showRepository;
    private final Faker faker;

    public ShowDataGenerator(ShowRepository showRepository) {
        this.showRepository = showRepository;
        faker = new Faker();
    }

    @PostConstruct
    private void generateShow() {
        if (showRepository.count() > 0) {
            LOGGER.info("show already generated");
        } else {
            LOGGER.info("generating {} show entries", NUMBER_OF_SHOWS_TO_GENERATE);
            for (int i = 0; i < NUMBER_OF_SHOWS_TO_GENERATE; i++) {

                /*
                private Event event;
                private Hall hall;
                */
                Show show = Show.builder()
                    .description(faker.lorem().characters(30, 40))
                    .durationInMinutes(faker.number().numberBetween(100,500))
                    .ticketsSold(faker.number().numberBetween(10,200))
                    .dateTime(
                        LocalDateTime.ofInstant(
                            faker.date().future((int) faker.random().nextDouble(), TimeUnit.DAYS).toInstant(), ZoneId.systemDefault()
                        )
                    )
                    .build();
                LOGGER.debug("saving show {}", show);
                showRepository.save(show);
            }
        }
    }
}