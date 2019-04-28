package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.User;

import java.util.List;

public interface IUserService {

    /**
     * Find all user entries.
     *
     * @return all users
     */
    List<User> findAll();

    /**
     * Find a single user entry by id.
     *
     * @param id the is of the user entry
     * @return the user entry
     */
    User findOne(Long id);


    /**
     * Create a user
     *
     * @param user to add
     * @return created user
     */
    User createUser(User user);

}
