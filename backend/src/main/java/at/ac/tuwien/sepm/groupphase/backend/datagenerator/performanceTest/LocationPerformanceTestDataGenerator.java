package at.ac.tuwien.sepm.groupphase.backend.datagenerator.performanceTest;

import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Profile("generatePerformanceTestData")
@Component
public class LocationPerformanceTestDataGenerator extends PerformanceTestDataGenerator {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final LocationRepository locationRepository;

    Faker faker = new Faker();

    public LocationPerformanceTestDataGenerator(LocationRepository locationRepository){
        this.locationRepository = locationRepository;
    }

    @Override
    public void generate(){
        if(locationRepository.count() > 0){
            LOGGER.info("Locations already generated");
        }else {
            LOGGER.info("Generating {} locations", NUM_OF_LOCATIONS);
            List<Location> locations = new ArrayList<>();
            for(Long id = 1L; id <= NUM_OF_LOCATIONS; id++) {
                locations.add(Location.builder()
                    .id(id)
                    .locationName(faker.beer().name())
                    .country("Austria")
                    .city(faker.address().city())
                    .postalCode(faker.numerify("####"))
                    .street(faker.address().streetAddress())
                    .description(faker.letterify("Description: ?????????????????"))
                    .build());
            }
            locationRepository.saveAll(locations);
        }
    }
}
