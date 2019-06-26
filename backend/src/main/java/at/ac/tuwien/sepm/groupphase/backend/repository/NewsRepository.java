package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<News, Long>, PagingAndSortingRepository<News, Long> {

    /**
     * Find a single news entry by id.
     *
     * @param id the is of the news entry
     * @return Optional containing the news entry
     */
    Optional<News> findOneById(Long id);

    /**
     * Find all news entries ordered by published at date (descending).
     * @param pageable special parameter to apply pagination
     * @return ordered page of all news entries
     */
    Page<News> findAllByOrderByPublishedAtDesc(Pageable pageable);
}
