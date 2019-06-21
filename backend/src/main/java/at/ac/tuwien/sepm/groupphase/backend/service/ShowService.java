package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.requestparameter.ShowRequestParameter;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.searchParameters.ShowSearchParametersDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.show.ShowDTO;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import org.springframework.data.domain.Page;

import java.util.List;

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
    Page<ShowDTO> findAllShowsFiltered(ShowSearchParametersDTO searchParameters, Integer page) throws ServiceException;

    /**
     * Find all shows filtered by location id
     *
     * @param id of a location to search for
     * @param page the number of the particular page to return
     * @return a page of the found shows
     * @throws ServiceException is thrown if something went wrong in the process
     */
    Page<ShowDTO> findAllShowsFilteredByLocationID(Long id, Integer page) throws ServiceException;

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
}
