package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom, PagingAndSortingRepository<User, Long> {

    /**
     * Find user entry by username.
     *
     * @param username the username of the desired user
     * @return Optional containing the user
     */
    Optional<User> findOneByUsername(String username);

}