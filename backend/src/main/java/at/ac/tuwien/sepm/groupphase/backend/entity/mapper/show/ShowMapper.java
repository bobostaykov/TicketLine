package at.ac.tuwien.sepm.groupphase.backend.entity.mapper.show;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.show.ShowDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShowMapper {

    Show showDTOToShow(ShowDTO showDTO);

    ShowDTO showToShowDTO(Show show);

}
