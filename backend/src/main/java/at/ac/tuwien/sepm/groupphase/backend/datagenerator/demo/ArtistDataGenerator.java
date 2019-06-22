package at.ac.tuwien.sepm.groupphase.backend.datagenerator.demo;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Profile("generateData")
@Component
public class ArtistDataGenerator implements DataGenerator{

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final ArtistRepository artistRepository;

    public ArtistDataGenerator(ArtistRepository artistRepository){
        this.artistRepository = artistRepository;
    }

    @Override
    public void generate(){
        if(artistRepository.count() > 0){
            LOGGER.info("Artists already generated");
        }else {
            LOGGER.info("Generating artists");
            Artist artist1 = Artist.builder().id(1L).name("Zara Holland").build();
            Artist artist2 = Artist.builder().id(2L).name("Benny Loshoto").build();
            Artist artist3 = Artist.builder().id(3L).name("Reggy").build();
            Artist artist4 = Artist.builder().id(4L).name("Tonio Bananas").build();
            Artist artist5 = Artist.builder().id(5L).name("Lady Galla").build();
            Artist artist6 = Artist.builder().id(6L).name("Pinna Colada").build();
            Artist artist7 = Artist.builder().id(7L).name("Steve Wonder").build();
            Artist artist8 = Artist.builder().id(8L).name("Johny Bravo").build();
            Artist artist9 = Artist.builder().id(9L).name("Perry Dont Merry").build();
            Artist artist10 = Artist.builder().id(10L).name("Real Thug").build();
            Artist artist11 = Artist.builder().id(11L).name("2Beta").build();
            Artist artist12 = Artist.builder().id(12L).name("Rice").build();
            Artist artist13 = Artist.builder().id(13L).name("Koch Suppe").build();

            artistRepository.saveAll(Arrays.asList(artist1, artist2, artist3, artist4, artist5,
                artist6, artist7, artist8, artist9, artist10, artist11, artist12, artist13));
        }
    }
}
