package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShowRepository;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

@Profile("generateData")
@Component
public class ShowDataGenerator implements DataGenerator{

    private static final Logger LOGGER = LoggerFactory.getLogger(ShowDataGenerator.class);

    private final ShowRepository showRepository;
    private final Faker faker;

    public ShowDataGenerator(ShowRepository showRepository) {
        this.showRepository = showRepository;
        faker = new Faker();
    }

    @Override
    public void generate() {

        if (showRepository.count() > 0) {
            LOGGER.info("Shows already generated");
        } else {
            LOGGER.info("Generating Show entries");
            /*
            for (int i = 0; i < NUMBER_OF_SHOWS_TO_GENERATE; i++) {


                //private Event event;
                //private Hall hall;

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
            LOGGER.debug("Saving show {}", show);
            showRepository.save(show);
        */
        }
    }
}