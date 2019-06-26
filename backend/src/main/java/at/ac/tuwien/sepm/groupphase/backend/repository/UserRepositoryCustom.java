package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.User;

import java.util.List;

public interface UserRepositoryCustom {
    /**
     * Creates the user and the associated logintable
     * @param user the user that is to be added
     * @return the added user
     */
    User createUser (User user);

    /**
     * Get list of all blocked users.
     *
     * @return a list of all currently blocked users
     */
    List<User> findAllBlockedUsers();
}
