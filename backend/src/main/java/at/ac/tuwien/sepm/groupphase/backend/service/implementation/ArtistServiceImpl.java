package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtistServiceImpl implements ArtistService {

    private final ArtistRepository artistRepository;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public ArtistServiceImpl(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    @Override
    public List<Artist> findArtistsByName(String artistName) {
//        List<Artist> list = artistRepository.findByNameContainingIgnoreCase(artistName);
//        LOGGER.error("\n\n\n" + list.get(0).getName() + "\n\n\n");
//        return list;
        return artistRepository.findByNameContainingIgnoreCase(artistName);
    }

}
