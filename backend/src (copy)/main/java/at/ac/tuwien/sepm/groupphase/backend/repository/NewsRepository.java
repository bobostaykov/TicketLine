package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    /**
     * Find a single news entry by id.
     *
     * @param id the is of the news entry
     * @return Optional containing the news entry
     */
    Optional<News> findOneById(Long id);

    /**
     * Find all news entries ordered by published at date (descending).
     *
     * @return ordered list of all news entries
     */
    List<News> findAllByOrderByPublishedAtDesc();

    /**
     * Find all news entries ordered by published at date (descending) which
     * are unread by user.
     *
     * @param givenId ID of user
     * @return ordered list of news entries
     * */
    @Query("SELECT m FROM News m LEFT JOIN UserNews u ON m.id = u.newsId WHERE NOT u.userId = :givenId ORDER BY m.publishedAt desc")
    List<News> findUnread(@Param("givenId") Long givenId);

}
