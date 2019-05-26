package at.ac.tuwien.sepm.groupphase.backend.repository;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.searchParameters.ShowSearchParametersDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;

import java.util.List;

public interface ShowRepositoryCustom {


    /**
     *
     * @param parameters a list of parameters that can be searched for
     *                   Event: name, type, description, duration
     *                   Hall: name
     *                   Location: country, city, postalcode, street,
     *                   Show: start- and end-Date (start date defaults on current date), start and end-time, min and max price
     * @return a List of shows that matches the criteria ordered by date
     */
    List<Show> findAllShowsFiltered(ShowSearchParametersDTO parameters);

}
