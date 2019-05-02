package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.UserNews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserNewsRepository extends JpaRepository<UserNews, Long> {
    /**
     * Find an entry if user with id userID has already read news with newsId.
     *
     * @param userId id of user
     * @param newsId id of news article
     * @return database entry that was found
     */
    Optional<UserNews> findOneByUserIdAndNewsId(Long userId, Long newsId);

    /**
     * Find all entries that have the user ID userID.
     * @param userId ID of the user
     * @return return a list of user and news ID combinations that were found
     */
    List<UserNews> findAllByUserId(Long userId);
}