package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.LoginAttempts;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface LoginAttemptsRepository extends JpaRepository<LoginAttempts, Long> {

    /**
     *
     * @param id
     * @return
     */
    Optional<LoginAttempts> findById(Long id);


    @Override
    LoginAttempts getOne(Long aLong);

    /**
     *
     * @return returns a list of all blocked user
     */
    List<LoginAttempts> getAllByBlockedTrue();

    /**
     *
     * @param user the user whose attempts are queried
     * @return the LoginAttempts of the user
     */
    LoginAttempts getByUser(User user);

    LoginAttempts getLoginAttemptsById(Long id);

    LoginAttempts findByUser(User user);
}
