package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.DetailedNewsDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.SimpleNewsDTO;

import java.util.List;

public interface NewsService {

    /**
     * Find all news entries ordered by published at date (descending).
     *
     * @return ordered list of al news entries
     */
    List<SimpleNewsDTO> findAll();

    /**
     * Find latest news entries ordered by published at date (descending).
     *
     * @param username the username to get the latest news for
     * @return ordered list of latest news entries
     */
    List<SimpleNewsDTO> findUnread(String username);

    /**
     * Find a single news entry by id.
     *
     * @param id the id of the news entry
     * @return the news entry
     */
    DetailedNewsDTO findOne(Long id);


    /**
     * Publish a single news entry
     *
     * @param detailedNewsDTO to publish
     * @return published news entry
     */
    DetailedNewsDTO publishNews(DetailedNewsDTO detailedNewsDTO);

}
