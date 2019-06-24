package at.ac.tuwien.sepm.groupphase.backend.datagenerator.performanceTest;

import at.ac.tuwien.sepm.groupphase.backend.datatype.PriceCategory;
import at.ac.tuwien.sepm.groupphase.backend.entity.PricePattern;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.HallRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PricePatternRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShowRepository;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.util.Map.entry;

@Profile("generatePerformanceTestData")
@Component
public class ShowPerformanceTestDataGenerator extends PerformanceTestDataGenerator {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final DateTimeFormatter timeFormatter;

    Faker faker = new Faker();

    private final ShowRepository showRepository;
    private final EventRepository eventRepository;
    private final HallRepository hallRepository;
    private final PricePatternRepository pricePatternRepository;

    public ShowPerformanceTestDataGenerator(ShowRepository showRepository, EventRepository eventRepository, HallRepository hallRepository,
                                            PricePatternRepository pricePatternRepository){
        this.showRepository = showRepository;
        this.eventRepository = eventRepository;
        this.hallRepository = hallRepository;
        this.pricePatternRepository = pricePatternRepository;
        timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    }


    @Override
    public void generate(){

        if(showRepository.count() > 0){
            LOGGER.info("Shows already generated");
        }else {
            LOGGER.info("Generating {} shows", NUM_OF_SHOWS);
            List<Show> shows = new ArrayList<>();
            for(Long id = 1L; id <= NUM_OF_SHOWS; id++) {
                PricePattern pricePattern = PricePattern.builder()
                    .setId(id)
                    .setName("Test Price Pattern")
                    .setPriceMapping(Map.ofEntries(
                        entry(PriceCategory.CHEAP, 13.0),
                        entry(PriceCategory.AVERAGE, 26.0),
                        entry(PriceCategory.EXPENSIVE, 52.0)))
                    .createPricePattern();
                pricePatternRepository.save(pricePattern);
                shows.add(Show.builder()
                    .id(id)
                    .event(eventRepository.getOne(customMod(id, NUM_OF_EVENTS)))
                    .hall(hallRepository.getOne(customMod(id, NUM_OF_HALLS)))
                    .date(faker.date().future(365, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusMonths(1))
                    .time(LocalTime.parse( "21:00",timeFormatter))
                    .description(faker.letterify("????????????????"))
                    .ticketsSold(0L)
                    .pricePattern(pricePatternRepository.getOne(id))
                    .build());
            }
            showRepository.saveAll(shows);
        }
    }
}
