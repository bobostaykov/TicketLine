package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.artist.ArtistDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.artist.ArtistMapper;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public Page<ArtistDTO> findArtistsByName(String artistName, Integer page) {
//        List<Artist> list = artistRepository.findByNameContainingIgnoreCase(artistName);
//        LOGGER.error("\n\n\n" + list.get(0).getUsername() + "\n\n\n");
//        return list;
        LOGGER.info("ArtistService: findArtistsByName");
        int pageSize = 10;
        if(page < 0) {
            throw new IllegalArgumentException("Not a valid page.");
        }
        Pageable pageable = PageRequest.of(page, pageSize);
        return artistRepository.findByNameContainingIgnoreCase(artistName, pageable).map(artistMapper::artistToArtistDTO);
    }

}
