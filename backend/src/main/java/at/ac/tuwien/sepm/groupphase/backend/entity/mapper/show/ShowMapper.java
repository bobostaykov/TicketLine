package at.ac.tuwien.sepm.groupphase.backend.entity.mapper.show;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.show.ShowDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ShowMapper {

    /**
     * Maps the ShowDTO object to Show object
     * @param showDTO to map
     * @return the mapped Show object
     */
    Show showDTOToShow(ShowDTO showDTO);

    /**
     * Maps the Show object to ShowDTO object
     * @param show to map
     * @return the mapped ShowDTO object
     */
    ShowDTO showToShowDTO(Show show);

    /**
     * Maps the List<Show> to List<ShowDTO>
     * @param shows to map
     * @return the mapped list of ShowDTO objects
     */
    List<ShowDTO> showToShowDTO(List<Show> shows);
}
