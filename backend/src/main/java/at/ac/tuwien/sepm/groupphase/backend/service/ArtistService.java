package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.artist.ArtistDTO;
import org.springframework.data.domain.Page;

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

}
