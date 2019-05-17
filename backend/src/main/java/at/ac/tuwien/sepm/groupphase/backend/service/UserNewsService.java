package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.userNews.UserNewsDTO;

public interface UserNewsService {

    /**
     * Persist latest news fetch date of user for specific news article
     *
     * @param userNewsDTO combination of user given by userID who read the news
     *                 article given by newsID.
     */
    void addNewsFetch(UserNewsDTO userNewsDTO);

}
