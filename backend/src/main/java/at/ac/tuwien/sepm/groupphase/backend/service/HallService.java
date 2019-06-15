package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.datatype.HallRequestParameters;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.hall.HallDTO;
import at.ac.tuwien.sepm.groupphase.backend.exception.CustomValidationException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;

import java.util.List;

public interface HallService {

    /**
     * gets a list of all halls found in the system
     * @param fields representing fields of hall entity that will be returned.
     * If null will return entire entity
     * @return list of all halls
     */
    List<HallDTO> findAllHalls(List<HallRequestParameters> fields);

    /**
     * Adds hall to the system
     * @param hallDTO dto of entity to be added
     * @return created hall als dto
     * @throws ServiceException if something goes wrong during data processing
     */
    HallDTO addHall(HallDTO hallDTO) throws ServiceException, CustomValidationException;

    /**
     * finds specific hall by id
     * @param hallId id of hall to be found
     * @return hall with specified id
     */
    HallDTO findHallById(Long hallId);
}
