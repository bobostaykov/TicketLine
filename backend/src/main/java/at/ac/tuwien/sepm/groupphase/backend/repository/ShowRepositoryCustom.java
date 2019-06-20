package at.ac.tuwien.sepm.groupphase.backend.repository;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.searchParameters.ShowSearchParametersDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

}
