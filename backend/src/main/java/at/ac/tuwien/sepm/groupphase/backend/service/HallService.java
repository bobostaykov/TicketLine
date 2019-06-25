package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.hall.HallDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.requestparameter.HallRequestParameter;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.searchParameters.HallSearchParametersDTO;
import at.ac.tuwien.sepm.groupphase.backend.exception.CustomValidationException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;

import java.util.List;

public interface HallService {

    /**
     * gets a list of halls found in the system filtered by search parameters if given
     * @param fields representing fields of hall entity that will be returned.
     * If null will return entire entity
     * @param searchParameters for which halls will be returned
     * if null will return all halls
     * @return list of all halls
     */
    List<HallDTO> findHalls(List<HallRequestParameter> fields, HallSearchParametersDTO searchParameters);

    /**
     * Adds hall to the system
     * @param hallDTO dto of entity to be added
     * @return created hall als dto
     * @throws ServiceException if something goes wrong during data processing
     * @throws CustomValidationException if hall already exists in the system
     */
    HallDTO addHall(HallDTO hallDTO) throws ServiceException, CustomValidationException;

    /**
     * finds specific hall by id
     * @param hallId id of hall to be found
     * @param include inclues special instructions for method.
     * If editing request parameter is set hall will return a boolean to decide hall should
     * be allowed to edit in frontend
     * @return hall with specified id
     */
    HallDTO findHallById(Long hallId, List<HallRequestParameter> include);

    /**
     * updates the passed as parameter
     * @param hallDTO to update hall to
     * @return updated hall as dto
     * @throws CustomValidationException if hall cannot be edited anymore because shows are associated with it
     */
    HallDTO updateHall(HallDTO hallDTO) throws CustomValidationException;

    /**
     * deletes the hall with the given id
     * @param hallId of hall to be deleted
     */
    void deleteHall(Long hallId);
}
