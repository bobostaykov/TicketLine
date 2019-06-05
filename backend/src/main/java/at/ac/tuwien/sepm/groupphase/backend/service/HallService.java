package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.hall.HallDTO;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;

import javax.xml.bind.ValidationException;
import java.util.List;

public interface HallService {

    /**
     * gets a list of all halls found in the system
     * @return list of all halls
     */
    List<HallDTO> findAllHalls();

    /**
     * Adds hall to the system
     * @param hallDTO dto of entity to be added
     * @return created hall als dto
     * @throws ServiceException if something goes wrong during data processing
     */
    HallDTO addHall(HallDTO hallDTO) throws ServiceException, at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
}
