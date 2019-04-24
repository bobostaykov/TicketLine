package at.ac.tuwien.sepm.groupphase.backend.service;

import java.time.LocalDateTime;

public interface UserService {

    /**
     * Persist latest news fetch date of user
     *
     * @param userName to be saved
     * @param timestamp to be saved
     */
    void addLatestNewsFetchDate(String userName, LocalDateTime timestamp);

}
