package at.ac.tuwien.sepm.groupphase.backend.datatype;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * all possible search parameters for which halls can be retrieved usind Requestparam fields
 */
public enum HallRequestParameters {
    @JsonProperty("id")
    ID,
    @JsonProperty("name")
    NAME,
    @JsonProperty("location")
    LOCATION
}
