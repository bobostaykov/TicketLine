package at.ac.tuwien.sepm.groupphase.backend.entity.mapper.Hall;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.hall.HallDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Hall;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HallMapper {

    Hall hallDTOToHall(HallDTO hallDTO);

    HallDTO hallToHallDTO(Hall hall);

    List<HallDTO> hallListToHallDTOs(List<Hall> halls);
}
