package at.ac.tuwien.sepm.groupphase.backend.datagenerator.performanceTest;

import at.ac.tuwien.sepm.groupphase.backend.datatype.EventType;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Profile("generatePerformanceTestData")
@Component
public class EventPerformanceTestDataGenerator extends PerformanceTestDataGenerator {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final EventRepository eventRepository;
    private final ArtistRepository artistRepository;

    Faker faker = new Faker();
    private static final Random RANDOM = new Random();

    public EventPerformanceTestDataGenerator(EventRepository eventRepository, ArtistRepository artistRepository){
        this.eventRepository = eventRepository;
        this.artistRepository = artistRepository;
    }

    private EventType getRandomEventType(){
        int x = RANDOM.nextInt(EventType.class.getEnumConstants().length);
        return EventType.class.getEnumConstants()[x];
    }

    @Override
    public void generate(){
        if(eventRepository.count() > 0){
            LOGGER.info("Events already generated");
        }else {
            LOGGER.info("Generating {} events", NUM_OF_EVENTS);
            List<Event> events = new ArrayList<>();
            for(Long id = 1L; id <= NUM_OF_EVENTS; id++) {
                events.add(Event.builder()
                    .id(id)
                    .name(faker.letterify("Event " + faker.hitchhikersGuideToTheGalaxy().character()) + " " + faker.ancient().hero())
                    .eventType(getRandomEventType())
                    .artist(artistRepository.getOne(customMod(id, NUM_OF_ARTISTS)))
                    .durationInMinutes(RANDOM.nextInt(MAX_EVENT_DURATION_IN_MINUTES-MIN_EVENT_DURATION_IN_MINUTES)+MIN_EVENT_DURATION_IN_MINUTES)
                    .description(faker.letterify("?????????????????"))
                    .content(faker.letterify("?????????????????"))
                    .build());
            }
            eventRepository.saveAll(events);
        }
    }
}
