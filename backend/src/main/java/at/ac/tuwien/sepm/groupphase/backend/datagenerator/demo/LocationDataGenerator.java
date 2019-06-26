package at.ac.tuwien.sepm.groupphase.backend.datagenerator.demo;

import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Profile("generateData")
@Component
public class LocationDataGenerator implements DataGenerator {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final LocationRepository locationRepository;

    public LocationDataGenerator(LocationRepository locationRepository){
        this.locationRepository = locationRepository;
    }

    @Override
    public void generate(){
        if(locationRepository.count() > 0){
            LOGGER.info("Locations already generated");
        }else {
            LOGGER.info("Generating locations");
            Location location1 = Location.builder().id(1L).locationName("Stadthalle").country("Austria").city("Vienna").postalCode("1090").street("Tendlergasse 12").build();
            Location location2 = Location.builder().id(2L).locationName("Stadthalle").country("Austria").city("Vienna").postalCode("1220").street("Josef Baumann Gasse 8").description("The new building").build();
            Location location3 = Location.builder().id(3L).locationName("Burgthater").country("Austria").city("Vienna").postalCode("1010").street("Maria-Theresien-Platz").build();
            Location location4 = Location.builder().id(4L).locationName("Stadthalle").country("Austria").city("Vienna").postalCode("1211").street("Siemensstraße 90").description("Behind hotel \"Azalia\"").build();
            Location location5 = Location.builder().id(5L).locationName("Stadthalle").country("Austria").city("Vienna").postalCode("1010").street("Burgring 7").build();
            Location location6 = Location.builder().id(6L).locationName("Burgthater").country("Austria").city("Vienna").postalCode("1030").street("Prinz Eugen-Straße 27").description("Looooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooong Description").build();
            Location location7 = Location.builder().id(7L).locationName("Stadthalle").country("Austria").city("Vienna").postalCode("1020").street("Oswald-Thomas-Platz 1").build();
            Location location8 = Location.builder().id(8L).locationName("Stadthalle").country("Austria").city("Graz").postalCode("8010").street("Schützenhofgasse 19").description("Next to the clock tower, on the left").build();
            Location location9 = Location.builder().id(9L).locationName("Burgthater").country("Austria").city("Graz").postalCode("8020").street("Absengerstraße 9").build();
            Location location10 = Location.builder().id(10L).locationName("Stadthalle").country("Austria").city("Graz").postalCode("8010").street("Heckenweg 10").build();

            locationRepository.saveAll(Arrays.asList(location1, location2, location3, location4, location5,
                location6, location7, location8, location9, location10));
        }
    }
}
