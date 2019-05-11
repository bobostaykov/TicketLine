package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.User;

public interface LoginAttemptService {
    /**
     *
     * @param username
     */
    void failedLogin(String username);

    /**
     *
     * @param username
     */
    void successfulLogin(String username);

    /**
     *
     */
    void unblockUser(User user);

    void blockUser(User user);
}
