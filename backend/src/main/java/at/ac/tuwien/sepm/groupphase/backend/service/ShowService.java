package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.searchParameters.ShowSearchParametersDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.show.ShowDTO;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;

public interface ShowService {

    /*
     * inds all shows filtered by event name
     * @param page the number of the page to return
     * @param eventName eventName name of event by which to filter the shows
     * @return a page of found shows matching the criteria
     */
    //Page<ShowDTO> findAllShowsFilteredByEventName(String eventName, Integer page);

    /**
     * Finds all shows
     * @param page number of the page to return, portion of all
     * @return a page of shows
     * @throws ServiceException is thrown if something went wrong in the process
     */
    Page<ShowDTO> findAll(Integer page) throws ServiceException;

    /*
     * Finds all shows filtered by location id
     *
     * @param locationID id of location by which to filter the shows
     * @return a list of found shows matching the criteria
     */
    //List<Show> findAllShowsFilteredByLocationID(Integer locationID);

    /*
     * Finds all shows filtered by date range, time range, price range, event name and hall username
     *
     * @param dateFrom lower bound of the date range in which the show is happening
     * @param dateTo upper bound of the date range in which the show is happening
     * @param timeFrom lower bound for staring time of the show
     * @param timeTo upper bound for starting time of the show
     * @param priceInEuroFrom minimal price in euros
     * @param priceInEuroTo maximal price in euros
     * @param eventName name of the event
     * @param hallName name of the hall
     * @return a list of found shows matching the criteria
     */

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
     * Delete the show with the given id
     *
     * @param showId of the show to delete
     * @throws ServiceException if the id is not found
     * @throws DataIntegrityViolationException if the entity can't be deleted because it's referenced by another one
     */
    void deleteById(Long showId) throws ServiceException, DataIntegrityViolationException;
}
