package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.UserNews;

public interface UserNewsService {

    /**
     * Persist latest news fetch date of user for specific news article
     *
     * @param userNews combination of user given by userID who read the news
     *                 article given by newsID.
     */
    void addNewsFetch(UserNews userNews);

}
