package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {

    /**
     * Get all artists whose name contains the string 'artistName'
     *
     * @return list of artists
     */
    List<Artist> findByNameContainingIgnoreCase(String artistName);

    /**
     * Get artists whose name is 'artistName'
     *
     * @return artist with such a name
     */
    Artist findByName(String artistName);
}
