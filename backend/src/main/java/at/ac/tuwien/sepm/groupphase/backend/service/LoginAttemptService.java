package at.ac.tuwien.sepm.groupphase.backend.service;

public interface LoginAttemptService {
    /**
     * checks if Username exists. If exists:
     * Increments the number of failed attempts and block the user in case of too many failed attempts
     *
     * @param username the name that was used in the login
     */
    void failedLogin(String username);

    /**
     * resets the number of failed attempts to 0;
     *
     * @param username the name of the user that logged in
     */
    void successfulLogin(String username);
}

