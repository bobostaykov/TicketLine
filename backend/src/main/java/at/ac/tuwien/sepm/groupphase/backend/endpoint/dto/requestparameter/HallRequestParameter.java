package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.requestparameter;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * all possible search parameters for which halls can be retrieved usind Requestparam fields
 */
public enum HallRequestParameter {
    @JsonProperty("id")
    ID,
    @JsonProperty("name")
    NAME,
    @JsonProperty("location")
    LOCATION,
    @JsonProperty("editing")
    EDITING
}
