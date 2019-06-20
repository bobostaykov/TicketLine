package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.artist.ArtistDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.artist.ArtistMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;


@Service
public class ArtistServiceImpl implements ArtistService {

    private final ArtistRepository artistRepository;
    private final EventRepository eventRepository;
    private final ArtistMapper artistMapper;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public ArtistServiceImpl(ArtistRepository artistRepository, ArtistMapper artistMapper, EventRepository eventRepository) {
        this.artistRepository = artistRepository;
        this.artistMapper = artistMapper;
        this.eventRepository = eventRepository;
    }

    @Override
    public Page<ArtistDTO> findArtistsByName(String artistName, Integer page, Integer pageSize) {
//        List<Artist> list = artistRepository.findByNameContainingIgnoreCase(artistName);
//        LOGGER.error("\n\n\n" + list.get(0).getUsername() + "\n\n\n");
//        return list;
        LOGGER.info("ArtistService: findArtistsByName");
        if(pageSize == null) {
            //default size
            pageSize = 10;
        }
        if (page < 0) {
            throw new IllegalArgumentException("Not a valid page.");
        }
        Pageable pageable = PageRequest.of(page, pageSize);
        return artistRepository.findByNameContainingIgnoreCase(artistName, pageable).map(artistMapper::artistToArtistDTO);
    }

    @Override
    public void deleteById(Long artistId) throws ServiceException, DataIntegrityViolationException {
        LOGGER.info("ArtistService: deleteById " + artistId);
        try {
            if (eventRepository.findAllByArtist_Id(artistId, PageRequest.of(0, 1)).hasContent())
                throw new DataIntegrityViolationException("Entity is referenced by an event! Deleting it will violate the referential integrity constaint.");
            artistRepository.deleteById(artistId);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }
}