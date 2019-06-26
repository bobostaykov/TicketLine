package at.ac.tuwien.sepm.groupphase.backend.entity.mapper.hall;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.hall.HallDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Hall;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HallMapper {

    /**
     * Maps the HallDTO object to Hall object
     * @param hallDTO to map
     * @return the mapped Hall object
     */
    Hall hallDTOToHall(HallDTO hallDTO);

    /**
     * Maps the Hall object to HallDTO object
     * @param hall to map
     * @return the mapped HallDTO object
     */
    HallDTO hallToHallDTO(Hall hall);

    /**
     * Maps the List<Hall> to List<HallDTO>
     * @param halls to map
     * @return the mapped list of HallDTO objects
     */
    List<HallDTO> hallListToHallDTOs(List<Hall> halls);
}
