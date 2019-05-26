package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.searchParameters.ShowSearchParametersDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.show.ShowDTO;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;

import java.util.List;

public interface ShowService {

    /**
     * Finds all shows filtered by event id
     *
     * @param eventName name of event by which to filter the shows
     * @return a list of found shows matching the criteria
     */
    List<ShowDTO> findAllShowsFilteredByEventName(String eventName);

    /**
     * Finds all shows
     *
     * @return a list of all shows
     */
    List<ShowDTO> findAll();

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
     * @param searchParameters: The parameter dto of criteria that can be searched for
     * @return a list of Shows that matches the specified criteria
     * @throws ServiceException is thrown if something went wrong in the process
     */
    List<ShowDTO> findAllShowsFiltered(ShowSearchParametersDTO searchParameters) throws ServiceException;

    /**
     *
     * @return a List of all shows(as a DTO) that are found in the database
     * @throws ServiceException is thrown if something went wrong in the process
     */
    List<ShowDTO> findAllShows() throws ServiceException;

    /**
     * Finds all shows filtered by country, city , postal code and street
     *
     * @param country the name of the country in which the show takes place
     * @param city the name of the city in which the show takes place
     * @param postalcode the postal code of the city in which the show takes place
     * @param street the street where the show takes place
     * @return a list of found shows matching the criteria
     */
    List<ShowDTO> findAllShowsFilteredByLocation(String country, String city, String postalcode, String street);

}
