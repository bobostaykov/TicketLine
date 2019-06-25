package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.artist.ArtistDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.artist.ArtistMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
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
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;


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
        LOGGER.info("ArtistService: findArtistsByName");
        if(pageSize == null) {
            //default size
            pageSize = 10;
        }
        if (page < 0) {
            throw new IllegalArgumentException("Not a valid page.");
        }
        Pageable pageable;
        if (artistName.equals("-1")) {
            artistName = "";
            pageable = Pageable.unpaged();
        } else pageable = PageRequest.of(page, pageSize);
        Page<ArtistDTO> result = artistRepository.findByNameContainingIgnoreCase(artistName, pageable).map(artistMapper::artistToArtistDTO);
        if (!result.hasContent()) throw new NotFoundException("No artist found");
        if (result.getContent().size() == 0) throw new NotFoundException("No artist found");
        return result;
    }

    @Override
    public ArtistDTO updateArtist(ArtistDTO artistDTO) throws ServiceException {
        LOGGER.info("ArtistService: updateArtist");
        try {
            return artistMapper.artistToArtistDTO(artistRepository.save(artistMapper.artistDTOToArtist(artistDTO)));
        } catch (DataIntegrityViolationException e) {
            throw new ServiceException("Artist name already exists.");
        }
        catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public ArtistDTO addArtist(ArtistDTO artistDTO) throws ServiceException {
        LOGGER.info("ArtistService: addArtist");
        try {
            return artistMapper.artistToArtistDTO(artistRepository.save(artistMapper.artistDTOToArtist(artistDTO)));
        } catch (DataIntegrityViolationException e) {
            throw new ServiceException("Artist name already exists.");
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
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