package at.ac.tuwien.sepm.groupphase.backend.entity.mapper.seat;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.seat.SeatDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SeatMapper {

    /**
     * converts seat entity to seatDTO
     * @param seat to be converted to dto
     * @return converted seatDTO
     */
    SeatDTO seatToSeatDTO(Seat seat);

    /**
     * converts seatDTO to seat entity
     * @param seatDTO to be converted
     * @return converted seat entity
     */
    Seat seatDTOToSeat(SeatDTO seatDTO);
}
