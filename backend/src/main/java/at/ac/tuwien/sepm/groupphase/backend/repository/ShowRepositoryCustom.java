package at.ac.tuwien.sepm.groupphase.backend.repository;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.searchParameters.ShowSearchParametersDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ShowRepositoryCustom {

    /**
     *
     * @param parameters a list of parameters that can be searched for
     *                   Event: name, type, description, duration
     *                   hall: name
     *                   Location: country, city, postal code, street,
     *                   Show: start- and end-Date (start date defaults on current date), start and end-time, min and max price
     * @param pageable number of the particular page to return, portion of the result, as well as size of the page
     * @return Page of shows that matches the criteria ordered by date
     */
    Page<Show> findAllShowsFiltered(ShowSearchParametersDTO parameters, Pageable pageable);

    /**
     * Finds a list of shows containing event name, date and time passed as param
     * @param eventName substring of name of event the show belongs to
     * @param date substring of show date
     * @param time substring of show time
     * @return a list of shows with only id, date and time matching search criteria
     */
    List<Show> findByEventNameAndShowDateAndShowTime(String eventName, String date, String time);
}
