package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.requestparameter.HallRequestParameter;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.searchParameters.HallSearchParametersDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Hall;

import java.util.List;

public interface HallRepositoryCustom {

    /**
     * returns a list of hall entities with only specified fields retrieved
     * @param fields to be retrieved from database for each hall entity
     * @return list of hall entitites with specified fields
     */
    List<Hall> findAllWithSpecifiedFields(List<HallRequestParameter> fields);

    /**
     * returns specified fields of hall entities filtered by search parameters
     * @param searchParametersDTO to filter halls by
     * @param fields to be retrieved from database for each hall entity
     * @return list of hall entities with specified fields that match search parameters
     */
    List<Hall> findAllFilteredWithSpecifiedFields(HallSearchParametersDTO searchParametersDTO, List<HallRequestParameter> fields);
}
