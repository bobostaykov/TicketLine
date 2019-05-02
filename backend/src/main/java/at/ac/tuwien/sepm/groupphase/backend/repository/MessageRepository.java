package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
     * Find all message entries ordered by published at date (descending) which
     * are unread by user.
     *
     * @param givenId ID of user
     * @return ordered list of message entries
     * */
    @Query("SELECT m FROM Message m LEFT JOIN UserNews u ON m.id = u.newsId WHERE NOT u.userId = :givenId ORDER BY m.publishedAt desc")
    List<Message> findUnread(@Param("givenId") Long givenId);

}
