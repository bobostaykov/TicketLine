package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.requestparameter.ShowRequestParameter;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.searchParameters.ShowSearchParametersDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.show.ShowDTO;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ShowService {

    /**
     * Finds all shows
     * @param page number of the page to return, portion of all
     * @return a page of shows
     * @throws ServiceException is thrown if something went wrong in the process
     */
    Page<ShowDTO> findAll(Integer page) throws ServiceException;

    /**
     *
     * @param searchParameters The parameter dto of criteria that can be searched for
     * @param page number of the particular page to return, portion of the result
     * @return a list of Shows that matches the specified criteria
     * @throws ServiceException is thrown if something went wrong in the process
     */
    Page<ShowDTO> findAllShowsFiltered(ShowSearchParametersDTO searchParameters, Integer page, Integer pageSize) throws ServiceException;

    /**
     * Find all shows filtered by location id
     *
     * @param id of a location to search for
     * @param page the number of the particular page to return
     * @return a page of the found shows
     * @throws ServiceException is thrown if something went wrong in the process
     */
    Page<ShowDTO> findAllShowsFilteredByLocationID(Long id, Integer page, Integer pageSize) throws ServiceException;

    /**
     * Finds a list of search suggestions for show and returns Simple Show projection containing id, eventName, date and time
     * @param eventName substring of name of event the show belongs to
     * @param date substring of show date
     * @param time substring of show time
     * @return list of shows containing only eventName, date and time
     */
    List<ShowDTO> findSearchResultSuggestions(String eventName, String date, String time);

    /**
     * Finds and returns one show by its id
     * @param id of show to be found
     * @param include list of show request parameters to perform additional operations if set
     * specifically will include ticketStatus for seats/sectors associated with the show
     * and price mapping of the show itself if correct parameters are sent
     * @return show with id matching param
     */
    ShowDTO findOneById(Long id, List<ShowRequestParameter> include);

    /**
     * Delete the show with the given id
     *
     * @param showId of the show to delete
     * @throws ServiceException if the id is not found
     * @throws DataIntegrityViolationException if the entity can't be deleted because it's referenced by another one
     */
    void deleteById(Long showId) throws ServiceException, DataIntegrityViolationException;

    /**
     * Change show information.
     *
     * @param showDTO show to be changed
     * @return changed show
     * @throws ServiceException if something goes wrong during updating
     * @throws IllegalArgumentException if the price pattern is not correct
     */
    ShowDTO updateShow(ShowDTO showDTO) throws ServiceException, IllegalArgumentException;

    /**
     * Add a show
     *
     * @param showDTO show to be added
     * @return added show
     * @throws ServiceException if something goes wrong during creation
     * @throws IllegalArgumentException if the price pattern is not correct
     */
    ShowDTO addShow(ShowDTO showDTO)  throws ServiceException, IllegalArgumentException;
}
