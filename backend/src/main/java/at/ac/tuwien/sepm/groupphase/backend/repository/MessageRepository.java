package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    /**
     * Find a single message entry by id.
     *
     * @param id the is of the message entry
     * @return Optional containing the message entry
     */
    Optional<Message> findOneById(Long id);

    /**
     * Find all message entries ordered by published at date (descending).
     *
     * @return ordered list of al message entries
     */
    List<Message> findAllByOrderByPublishedAtDesc();

    /**
     * Find all message entries ordered by published at date (descending) which where created later than timestamp.
     *
     * @param timestamp search for all news, which where created after this datetime
     * @return ordered list of message entries
     */
    @Query("SELECT n FROM Message n WHERE n.publishedAt >= :timestamp")
    List<Message> findAllAfter(@Param("timestamp") LocalDateTime timestamp);

}
