package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;

import java.util.List;

public interface ArtistService {

    /**
     * Get all artists whose name contains the string 'artistName'
     *
     * @param artistName string by which to filter
     * @return a list of suitable artists
     */
    List<Artist> findArtistsByName(String artistName);

}
