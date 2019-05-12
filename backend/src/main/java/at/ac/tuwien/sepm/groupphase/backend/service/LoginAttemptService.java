package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.User;

public interface LoginAttemptService {
    /**
     * checks if Username exists. If exists:
     * Increments the number of failed attempts and block the user in case of too many failed attempts
     * @param username the name that was used in the login
     */
    void failedLogin(String username);

    /**
     * resets the number of failed attempts to 0;
     * @param username the name of the user that logged in
     */
    void successfulLogin(String username);


    /**
     * unblocks a user
     * @param user the entity of the user
     */
    void unblockUser(User user);

    /**
     * unblocks a user identified by hist id;
     * @param id the id
     */
    void unblockUserById(Long id);

    /**
     * blocks a user
     * @param user the user that is to be blocked;
     */
    void blockUser(User user);

    /**
     * creates a Loginattempts entry for new created user
     * @param user
     */
    void initializeLoginAttempts(User user);
}
