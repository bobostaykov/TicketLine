package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.artist.ArtistDTO;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;

import java.sql.SQLException;
import java.util.List;

public interface ArtistService {

    /**
     * Get all artists whose name contains the string 'artistName'
     *
     * @param artistName string by which to filter
     * @param page number of the page to return
     * @return a list of suitable artists
     */
    Page<ArtistDTO> findArtistsByName(String artistName, Integer page);

    /**
     * Delete the artist with the given Id
     *
     * @param artistId of the artist to delete
     * @throws ServiceException if the id is not found
     * @throws DataIntegrityViolationException if the entity can't be deleted because it's referenced by another one
     */
    void deleteById(Long artistId) throws ServiceException, DataIntegrityViolationException;

    /**
     * Updates the given artist
     * @param artistDTO the artist to change
     * @return changed artist
     */
    ArtistDTO updateArtist(ArtistDTO artistDTO);
}
