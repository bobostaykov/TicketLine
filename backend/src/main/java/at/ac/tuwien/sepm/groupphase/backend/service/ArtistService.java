package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.artist.ArtistDTO;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;

public interface ArtistService {

    /**
     * Get all artists whose name contains the string 'artistName'
     *
     * @param artistName string by which to filter
     * @param page number of the page to return
     * @return a list of suitable artists
     */
    Page<ArtistDTO> findArtistsByName(String artistName, Integer page, Integer pageSize);

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
     * @throws ServiceException if something goes wrong during update
     */
    ArtistDTO updateArtist(ArtistDTO artistDTO) throws ServiceException;

    /**
     * Add the given artist
     * @param artistDTO the artist to add
     * @return added artist
     * @throws ServiceException if something goes wrong during creation
     */
    ArtistDTO addArtist(ArtistDTO artistDTO) throws ServiceException;
}
