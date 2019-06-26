package at.ac.tuwien.sepm.groupphase.backend.datagenerator.performanceTest;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Profile("generatePerformanceTestData")
@Component
public class ArtistPerformanceTestDataGenerator extends PerformanceTestDataGenerator {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final ArtistRepository artistRepository;

    Faker faker = new Faker();

    public ArtistPerformanceTestDataGenerator(ArtistRepository artistRepository){
        this.artistRepository = artistRepository;
    }

    @Override
    public void generate(){
        if(artistRepository.count() > 0){
            LOGGER.info("Artists already generated");
        }else {
            LOGGER.info("Generating {} artists", NUM_OF_ARTISTS);
            List<Artist> artists = new ArrayList<>();
            for(Long id = 1L; id <= NUM_OF_ARTISTS; id++) {
                artists.add(Artist.builder()
                    .id(id)
                    .name(faker.ancient().god() + " " + faker.letterify("?????????"))
                    .build());
            }
            artistRepository.saveAll(artists);
        }
    }
}
