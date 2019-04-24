package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user entry by username.
     *
     * @param userName the username of the desired user
     * @return Optional containing the user
     */
    Optional<User> findOneByUserName(String userName);

    /**
     * Update last news fetch timestamp of specified user
     *
     * @param userName the username of the desired user
     * @return Optional containing the user
     */
    @Query("UPDATE User SET lastFetchTimestamp = timestamp WHERE userName = :userName")
    Optional<User> updateLastNewsFetchTimestampByUserName(String userName, LocalDateTime timestamp);


}
