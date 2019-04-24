package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.UserLatestNewsFetch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserLastNewsFetchRepository extends JpaRepository<UserLatestNewsFetch, Long> {

    /**
     * Find latest news fetch timestamp by username.
     *
     * @param userName the username from whom to find the latest newd fetch timestamp
     * @return Optional containing the latest fetch timestamp
     */
    Optional<UserLatestNewsFetch> findOneByUserName(String userName);

}
