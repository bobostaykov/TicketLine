package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.datatype.HallRequestParameters;
import at.ac.tuwien.sepm.groupphase.backend.entity.Hall;

import java.util.List;

public interface HallRepositoryCustom {

    /**
     * returns a list of hall entities with only specified fields retrieved
     * @param fields to be retrieved from database for each hall entity
     * @return list of hall entitites with specified fields
     */
    List<Hall> findAllWithSpecifiedFields(List<HallRequestParameters> fields);
}
