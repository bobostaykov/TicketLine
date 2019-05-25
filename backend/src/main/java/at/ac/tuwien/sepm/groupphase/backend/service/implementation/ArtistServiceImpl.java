package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.artist.ArtistDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.artist.ArtistMapper;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtistServiceImpl implements ArtistService {

    private final ArtistRepository artistRepository;
    private final ArtistMapper artistMapper;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public ArtistServiceImpl(ArtistRepository artistRepository, ArtistMapper artistMapper) {
        this.artistRepository = artistRepository;
        this.artistMapper = artistMapper;
    }

    @Override
    public List<ArtistDTO> findArtistsByName(String artistName) {
//        List<Artist> list = artistRepository.findByNameContainingIgnoreCase(artistName);
//        LOGGER.error("\n\n\n" + list.get(0).getUsername() + "\n\n\n");
//        return list;
        LOGGER.info("Artist Service: findArtistsByName");
        return artistMapper.artistToArtistDTO(artistRepository.findByNameContainingIgnoreCase(artistName));
    }

}
