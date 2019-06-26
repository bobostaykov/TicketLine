package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.List;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long>, PagingAndSortingRepository<Artist, Long> {

    /**
     * Get all artists whose name contains the string 'artistName'
     * @param artistName to search for
     * @param pageable pageable special parameter to apply pagination
     * @return a page of the found artists
     */
    Page<Artist> findByNameContainingIgnoreCase(String artistName, Pageable pageable);

    /**
     * Get artists whose name is 'artistName'
     *
     * @return artist with such a name
     */
    Artist findByName(String artistName);

    /**
     * Deletes the artist with the given id
     *
     * @param artistId to delete
     */
    void deleteById(Long artistId);

    List<Artist> findAllByName(String name);
}
