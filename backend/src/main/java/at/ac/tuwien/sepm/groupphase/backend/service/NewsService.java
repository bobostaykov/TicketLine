package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.DetailedNewsDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.SimpleNewsDTO;
import org.springframework.data.domain.Page;

public interface NewsService {

    /**
     * Find all news entries ordered by published at date (descending).
     * @param page the number of the requested page
     * @param pageSize number of entries per page
     * @return ordered page of al news entries
     */
    Page<SimpleNewsDTO> findAll(Integer page, Integer pageSize);


    /**
     * Find latest news entries ordered by published at date (descending).
     * @param page the number of the requested page
     * @param pageSize number of entries per page
     * @return ordered page of latest news entries
     */
    Page<SimpleNewsDTO> findUnread(String username, Integer page, Integer pageSize);

    /**
     * Find a single news entry by id.
     *
     * @param id the id of the news entry
     * @return the news entry
     */
    DetailedNewsDTO findOne(Long id, String username);


    /**
     * Publish a single news entry
     *
     * @param detailedNewsDTO to publish
     * @return published news entry
     */
    DetailedNewsDTO publishNews(DetailedNewsDTO detailedNewsDTO);

    /**
     * Add news fetch to news list of user.
     *
     * @param username name of user
     * @param news_id ID of news that was fetched by user
     */
    void addNewsFetch(String username, Long news_id);

}
