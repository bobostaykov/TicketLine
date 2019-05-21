package at.ac.tuwien.sepm.groupphase.backend.repository;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.searchParameters.ShowSearchParametersDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;

import java.util.List;

public interface ShowRepositoryCustom {


    List<Show> findAllShowsFiltered(ShowSearchParametersDTO parameters);

}
